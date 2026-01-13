package tcbv.zhaohui.moon.service.impl;

import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.dao.BullfightingScoreDao;
import tcbv.zhaohui.moon.entity.BullfightingRecordEntity;
import tcbv.zhaohui.moon.dao.BullfightingRecordDao;
import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.BullfightingRecordService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.BizException.BULLFIGHTING_TIMES_EXHAUSTEDLY;

/**
 * 斗牛游戏记录(BullfightingRecord)表服务实现类
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
@Service("bullfightingRecordService")
public class BullfightingRecordServiceImpl implements BullfightingRecordService {
    @Resource
    private BullfightingRecordDao bullfightingRecordDao;

    @Resource
    private BullfightingScoreDao bullfightingScoreDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BullfightingRecordEntity queryById(String id) {
        return this.bullfightingRecordDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param bullfightingRecordEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<BullfightingRecordEntity> queryByPage(BullfightingRecordEntity bullfightingRecordEntity, PageRequest pageRequest) {
        long total = this.bullfightingRecordDao.count(bullfightingRecordEntity);
        return new PageImpl<>(this.bullfightingRecordDao.queryAllByLimit(bullfightingRecordEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param bullfightingRecordEntity 实例对象
     * @return 实例对象
     */
    @Override
    public BullfightingRecordEntity insert(BullfightingRecordEntity bullfightingRecordEntity) {
        bullfightingRecordEntity.setId(UUID.randomUUID().toString());
        this.bullfightingRecordDao.insert(bullfightingRecordEntity);
        return bullfightingRecordEntity;
    }

    /**
     * 修改数据
     *
     * @param bullfightingRecordEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(BullfightingRecordEntity bullfightingRecordEntity) {
        return this.bullfightingRecordDao.update(bullfightingRecordEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.bullfightingRecordDao.deleteById(id) > 0;
    }

    @Override
    @Transactional
    public BullfightingScoreEntity save(BullfightingRecordEntity bullfightingRecordEntity) {
        Date gameDate = new Date();
        BullfightingScoreEntity bullfightingScoreEntity = bullfightingScoreDao.queryByUserId(bullfightingRecordEntity.getUserId(), gameDate);
        if (bullfightingScoreEntity != null && bullfightingScoreEntity.getTimes() != null && bullfightingScoreEntity.getTimes() < 1) {
            throw new BizException(BULLFIGHTING_TIMES_EXHAUSTEDLY,"今日游戏次数已用完");
        }
        int recordScore = bullfightingRecordEntity.getScore();
        int score = Boolean.TRUE.equals(bullfightingRecordEntity.getWinlose()) ? recordScore : -recordScore;
        if (bullfightingScoreEntity == null) {
            bullfightingScoreEntity = new BullfightingScoreEntity();
            bullfightingScoreEntity.setId(UUID.randomUUID().toString());
            bullfightingScoreEntity.setUserId(bullfightingRecordEntity.getUserId());
            bullfightingScoreEntity.setScore(score);
            bullfightingScoreEntity.setTimes(4);
            bullfightingScoreEntity.setGameDate(gameDate);
            bullfightingScoreDao.insert(bullfightingScoreEntity);
        } else {
            bullfightingScoreEntity.setScore(bullfightingScoreEntity.getScore() + score);
            bullfightingScoreEntity.setTimes(bullfightingScoreEntity.getTimes() - 1);
            bullfightingScoreDao.update(bullfightingScoreEntity);
        }
        bullfightingRecordEntity.setCreateTime(gameDate);
        this.insert(bullfightingRecordEntity);
        return bullfightingScoreEntity;
    }
}
