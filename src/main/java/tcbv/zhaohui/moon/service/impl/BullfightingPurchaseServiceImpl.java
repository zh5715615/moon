package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.dao.BullfightingScoreDao;
import tcbv.zhaohui.moon.entity.BullfightingPurchaseEntity;
import tcbv.zhaohui.moon.dao.BullfightingPurchaseDao;
import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.BullfightingPurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.BizException.BULLFIGHTING_TIMES_EXHAUSTEDLY;

/**
 * 购买斗牛次数记录表(BullfightingPurchase)表服务实现类
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Service("bullfightingPurchaseService")
public class BullfightingPurchaseServiceImpl implements BullfightingPurchaseService {
    @Resource
    private BullfightingPurchaseDao bullfightingPurchaseDao;

    @Resource
    private BullfightingScoreDao bullfightingScoreDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BullfightingPurchaseEntity queryById(String id) {
        return this.bullfightingPurchaseDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param bullfightingPurchaseEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<BullfightingPurchaseEntity> queryByPage(BullfightingPurchaseEntity bullfightingPurchaseEntity, PageRequest pageRequest) {
        long total = this.bullfightingPurchaseDao.count(bullfightingPurchaseEntity);
        return new PageImpl<>(this.bullfightingPurchaseDao.queryAllByLimit(bullfightingPurchaseEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 实例对象
     */
    @Override
    public BullfightingPurchaseEntity insert(BullfightingPurchaseEntity bullfightingPurchaseEntity) {
        bullfightingPurchaseEntity.setId(UUID.randomUUID().toString());
        Date gameDate = new Date();
        bullfightingPurchaseEntity.setCreateTime(gameDate);
        this.bullfightingPurchaseDao.insert(bullfightingPurchaseEntity);

        BullfightingScoreEntity bullfightingScoreEntity = bullfightingScoreDao.queryByUserId(bullfightingPurchaseEntity.getUserId(), gameDate);
        int times = bullfightingPurchaseEntity.getTimes();
        if (bullfightingScoreEntity != null) {
            times += bullfightingScoreEntity.getTimes();
            BullfightingScoreEntity updateEntity = new BullfightingScoreEntity();
            updateEntity.setId(bullfightingScoreEntity.getId());
            updateEntity.setTimes(times);
            bullfightingScoreDao.update(updateEntity);
        } else {
            bullfightingScoreEntity = new BullfightingScoreEntity();
            bullfightingScoreEntity.setUserId(bullfightingPurchaseEntity.getUserId());
            bullfightingScoreEntity.setTimes(times);
            bullfightingScoreEntity.setUserId(bullfightingPurchaseEntity.getUserId());
            bullfightingScoreEntity.setScore(0);
            bullfightingScoreEntity.setGameDate(gameDate);
            bullfightingScoreDao.insert(bullfightingScoreEntity);
        }
        return bullfightingPurchaseEntity;
    }

    /**
     * 修改数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(BullfightingPurchaseEntity bullfightingPurchaseEntity) {
        return this.bullfightingPurchaseDao.update(bullfightingPurchaseEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.bullfightingPurchaseDao.deleteById(id) > 0;
    }
}
