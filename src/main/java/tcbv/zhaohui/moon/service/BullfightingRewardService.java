package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.BullfightingRewardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 斗牛游戏领取奖励记录(BullfightingReward)表服务接口
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
public interface BullfightingRewardService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingRewardEntity queryById(String id);

    /**
     * 分页查询
     *
     * @param bullfightingRewardEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<BullfightingRewardEntity> queryByPage(BullfightingRewardEntity bullfightingRewardEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 实例对象
     */
    BullfightingRewardEntity insert(BullfightingRewardEntity bullfightingRewardEntity);

    /**
     * 修改数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 实例对象
     */
    boolean update(BullfightingRewardEntity bullfightingRewardEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);
}
