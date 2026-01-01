package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.dao.PledgePromotionDao;
import tcbv.zhaohui.moon.dao.UserDao;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.dao.PledgeDao;
import tcbv.zhaohui.moon.entity.PledgePromotionEntity;
import tcbv.zhaohui.moon.entity.UserEntity;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.exceptions.DBException;
import tcbv.zhaohui.moon.service.PledgeService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.service.chain.Token20Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.BizException.*;
import static tcbv.zhaohui.moon.exceptions.DBException.INSERT_FAILED;

/**
 * 质押表(Pledge)表服务实现类
 *
 * @author makejava
 * @since 2025-12-21 11:01:17
 */
@Service("pledgeService")
@Slf4j
public class PledgeServiceImpl implements PledgeService {
    @Resource
    private PledgeDao pledgeDao;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PledgePromotionDao pledgePromotionDao;


    @Value("${star-wars.promote-reward-percentage}")
    private double promoteRewardPercentage;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public PledgeEntity queryById(String id) {
        return this.pledgeDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param pledgeEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PledgeEntity> queryByPage(PledgeEntity pledgeEntity, PageRequest pageRequest) {
        long total = this.pledgeDao.count(pledgeEntity);
        return new PageImpl<>(this.pledgeDao.queryAllByLimit(pledgeEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param pledgeEntity 实例对象
     * @return 实例对象
     */
    @Override
    @Transactional
    public PledgeEntity insert(PledgeEntity pledgeEntity) {
        if (pledgeEntity == null) {
            log.error("insert pledgeEntity is null");
            throw new BizException(NULL_EXCEPTION, "pledgeEntity is null");
        }

        final String txHash = pledgeEntity.getPledgeHash();
        final String userId = pledgeEntity.getUserId();

        if (StringUtils.isBlank(txHash)) {
            log.error("insert pledge failed: pledgeHash is blank, pledge={}", pledgeEntity);
            throw new BizException(NULL_EXCEPTION, "pledgeHash is blank");
        }
        if (StringUtils.isBlank(userId)) {
            log.error("insert pledge failed: userId is blank, txHash={}, pledge={}", txHash, pledgeEntity);
            throw new BizException(NULL_EXCEPTION, "userId is blank");
        }

        // 幂等校验：同一个 txHash 只允许处理一次
        PledgeEntity existed = pledgeDao.queryByPledgeHash(txHash);
        if (existed != null) {
            log.warn("pledge hash already exists: txHash={}, existedPledgeId={}", txHash, existed.getId());
            throw new BizException(HASH_ALREADY_HANDLE, "pledge hash[" + txHash + "] is exist");
        }

        final String pledgeId = UUID.randomUUID().toString();
        pledgeEntity.setId(pledgeId);

        int insertCnt;
        try {
            insertCnt = pledgeDao.insert(pledgeEntity);
        } catch (Exception e) {
            log.error("insert pledge db failed: pledgeId={}, txHash={}, userId={}, pledge={}",
                    pledgeId, txHash, userId, pledgeEntity, e);
            throw new DBException(INSERT_FAILED, "插入质押表失败: " + e.getMessage());
        }

        if (insertCnt <= 0) {
            log.error("insert pledge affected rows <= 0: pledgeId={}, txHash={}, userId={}",
                    pledgeId, txHash, userId);
            throw new DBException(INSERT_FAILED, "插入质押表失败" );
        }

        // 不满足奖励条件直接结束（减少嵌套）
        if (!isHighLevelPledge(pledgeEntity)) {
            return pledgeEntity;
        }

        // ======= 奖励逻辑（尽量稳） =======
        tryRewardPromotion(pledgeEntity, pledgeId, txHash, userId);

        return pledgeEntity;
    }

    private boolean isHighLevelPledge(PledgeEntity pledgeEntity) {
        int region = pledgeEntity.getRegion();
        return region == PledgeRegion.HIGH.getLevel()
                || region == PledgeRegion.FINAL.getLevel();
    }

    private void tryRewardPromotion(PledgeEntity pledgeEntity, String pledgeId, String txHash, String userId) {
        UserEntity userEntity;
        try {
            userEntity = userDao.queryById(userId);
        } catch (Exception e) {
            log.error("query user failed: userId={}, pledgeId={}, txHash={}", userId, pledgeId, txHash, e);
            return; // 奖励失败不影响质押入库（按你业务：也可以选择 throw 回滚）
        }

        if (userEntity == null) {
            log.warn("skip reward: user not found, userId={}, pledgeId={}, txHash={}", userId, pledgeId, txHash);
            return;
        }

        String parentId = userEntity.getParentId();
        if (StringUtils.isBlank(parentId)) {
            log.info("skip reward: parentId is blank, userId={}, pledgeId={}, txHash={}", userId, pledgeId, txHash);
            return;
        }

        UserEntity parentEntity;
        try {
            parentEntity = userDao.queryById(parentId);
        } catch (Exception e) {
            log.error("query parent user failed: parentId={}, userId={}, pledgeId={}, txHash={}",
                    parentId, userId, pledgeId, txHash, e);
            return;
        }

        if (parentEntity == null) {
            log.warn("skip reward: parent not found, parentId={}, userId={}, pledgeId={}, txHash={}",
                    parentId, userId, pledgeId, txHash);
            return;
        }

        String parentAddress = parentEntity.getAddress();
        if (StringUtils.isBlank(parentAddress)) {
            log.warn("skip reward: parent address is blank, parentId={}, userId={}, pledgeId={}, txHash={}",
                    parentId, userId, pledgeId, txHash);
            return;
        }

        // 判断是否已经奖励过（幂等）
        PledgePromotionEntity existedPromotion;
        try {
            existedPromotion = pledgePromotionDao.queryByIdAndPromoter(userId, parentId);
        } catch (Exception e) {
            log.error("query promotion failed: userId={}, parentId={}, pledgeId={}, txHash={}",
                    userId, parentId, pledgeId, txHash, e);
            return;
        }

        if (existedPromotion != null) {
            log.info("skip reward: promotion already exists, userId={}, parentId={}, pledgeId={}, txHash={}, promotionId={}",
                    userId, parentId, pledgeId, txHash, existedPromotion.getId());
            return;
        }

        double amount = pledgeEntity.getAmount();
        if (amount <= 0) {
            log.warn("skip reward: pledge amount <= 0, amount={}, userId={}, parentId={}, pledgeId={}, txHash={}",
                    amount, userId, parentId, pledgeId, txHash);
            return;
        }

        double rewardAmount = amount * promoteRewardPercentage;
        if (rewardAmount <= 0) {
            log.warn("skip reward: rewardAmount <= 0, rewardAmount={}, percentage={}, amount={}, userId={}, parentId={}, pledgeId={}, txHash={}",
                    rewardAmount, promoteRewardPercentage, amount, userId, parentId, pledgeId, txHash);
            return;
        }

        // 先链上转账，再写奖励记录（你也可以相反：先落库pending，再转账；更易补偿）
        String rewardHash;
        try {
            rewardHash = spaceJediService.transfer(parentAddress, BigDecimal.valueOf(rewardAmount));
        } catch (Exception e) {
            log.error("reward transfer failed: to={}, rewardAmount={}, userId={}, parentId={}, pledgeId={}, txHash={}",
                    parentAddress, rewardAmount, userId, parentId, pledgeId, txHash, e);
            return; // 不抛出，避免影响主流程（按你业务决定）
        }

        PledgePromotionEntity promotionEntity = new PledgePromotionEntity();
        promotionEntity.setPledgeId(pledgeId);
        promotionEntity.setPromoterId(parentId);
        promotionEntity.setRewardAmount(rewardAmount);
        promotionEntity.setCreateTime(pledgeEntity.getCreateTime());
        promotionEntity.setHash(rewardHash);

        try {
            pledgePromotionDao.insert(promotionEntity);
            log.info("reward promotion success: rewardHash={}, rewardAmount={}, userId={}, parentId={}, pledgeId={}, txHash={}",
                    rewardHash, rewardAmount, userId, parentId, pledgeId, txHash);
        } catch (Exception e) {
            // ⚠️ TODO 优化选项，链上已经转了，但落库失败，需要你有补偿/对账机制
            log.error("insert promotion record failed AFTER transfer: rewardHash={}, rewardAmount={}, userId={}, parentId={}, pledgeId={}, txHash={}",
                    rewardHash, rewardAmount, userId, parentId, pledgeId, txHash, e);
            // 可以选择抛出让事务回滚，但链上转账无法回滚，会造成“已转账但记录没了”，所以多数情况下：不抛出，记录错误，靠对账补偿。
        }
    }


    /**
     * 修改数据
     *
     * @param pledgeEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(PledgeEntity pledgeEntity) {
        return this.pledgeDao.update(pledgeEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.pledgeDao.deleteById(id) > 0;
    }

    @Override
    public PledgeEntity withdraw(PledgeEntity pledgeEntity) {
        String id = pledgeEntity.getId();
        PledgeEntity queryEntity = pledgeDao.queryById(id);
        if (!queryEntity.getUserId().equals(pledgeEntity.getUserId())) {
            throw new BizException(USER_NOT_MATCH, "Pledge not belong to user");
        }
        if (pledgeEntity.getRegion() != queryEntity.getRegion()) {
            throw new BizException(REGION_NOT_MATCH, "Region not match");
        }

        Date now = new Date();
        if (queryEntity.getExpireTime().after(now)) {
            log.warn("skip withdraw: pledge not expired, expireTime={}, now={}", pledgeEntity.getExpireTime(), now);
        }
        pledgeEntity.setWithdrawTime(now);
        return pledgeDao.update(pledgeEntity) > 0 ? pledgeEntity : null;
    }
}
