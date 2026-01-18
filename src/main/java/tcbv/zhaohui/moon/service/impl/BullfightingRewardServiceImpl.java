package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.BullfightingRewardEntity;
import tcbv.zhaohui.moon.dao.BullfightingRewardDao;
import tcbv.zhaohui.moon.service.BullfightingRewardService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 斗牛游戏领取奖励记录(BullfightingReward)表服务实现类
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Service("bullfightingRewardService")
public class BullfightingRewardServiceImpl implements BullfightingRewardService {
    @Resource
    private BullfightingRewardDao bullfightingRewardDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public BullfightingRewardEntity queryById(String id) {
        return this.bullfightingRewardDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param bullfightingRewardEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<BullfightingRewardEntity> queryByPage(BullfightingRewardEntity bullfightingRewardEntity, PageRequest pageRequest) {
        long total = this.bullfightingRewardDao.count(bullfightingRewardEntity);
        return new PageImpl<>(this.bullfightingRewardDao.queryAllByLimit(bullfightingRewardEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 实例对象
     */
    @Override
    public BullfightingRewardEntity insert(BullfightingRewardEntity bullfightingRewardEntity) {
        bullfightingRewardEntity.setId(UUID.randomUUID().toString());
        bullfightingRewardEntity.setCreateTime(new Date());
        this.bullfightingRewardDao.insert(bullfightingRewardEntity);
        return bullfightingRewardEntity;
    }

    /**
     * 修改数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(BullfightingRewardEntity bullfightingRewardEntity) {
        return this.bullfightingRewardDao.update(bullfightingRewardEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String id) {
        return this.bullfightingRewardDao.deleteById(id) > 0;
    }
}
