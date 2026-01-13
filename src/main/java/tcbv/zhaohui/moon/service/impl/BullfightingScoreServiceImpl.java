package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import tcbv.zhaohui.moon.dao.BullfightingScoreDao;
import tcbv.zhaohui.moon.service.BullfightingScoreService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.vo.BullfightingUserStatisticsVo;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 斗牛用户积分(BullfightingScore)表服务实现类
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
@Service("bullfightingScoreService")
public class BullfightingScoreServiceImpl implements BullfightingScoreService {
    @Resource
    private BullfightingScoreDao bullfightingScoreDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BullfightingScoreEntity queryById(String id) {
        return this.bullfightingScoreDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param bullfightingScoreEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<BullfightingScoreEntity> queryByPage(BullfightingScoreEntity bullfightingScoreEntity, PageRequest pageRequest) {
        long total = this.bullfightingScoreDao.count(bullfightingScoreEntity);
        return new PageImpl<>(this.bullfightingScoreDao.queryAllByLimit(bullfightingScoreEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 实例对象
     */
    @Override
    public BullfightingScoreEntity insert(BullfightingScoreEntity bullfightingScoreEntity) {
                    bullfightingScoreEntity.setId(UUID.randomUUID().toString());
        this.bullfightingScoreDao.insert(bullfightingScoreEntity);
        return bullfightingScoreEntity;
    }

    /**
     * 修改数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(BullfightingScoreEntity bullfightingScoreEntity) {
        return this.bullfightingScoreDao.update(bullfightingScoreEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.bullfightingScoreDao.deleteById(id) > 0;
    }

    @Override
    public BullfightingUserStatisticsVo userStatistics(String userId, Date gameDate) {
        return bullfightingScoreDao.statictics(userId, gameDate);
    }
}
