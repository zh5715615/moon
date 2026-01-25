package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.BullfightingPurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

/**
 * 购买斗牛次数记录表(BullfightingPurchase)表服务接口
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
public interface BullfightingPurchaseService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingPurchaseEntity queryById(String id);

    /**
     * 分页查询
     *
     * @param bullfightingPurchaseEntity 筛选条件
     * @param pageRequest                分页对象
     * @return 查询结果
     */
    Page<BullfightingPurchaseEntity> queryByPage(BullfightingPurchaseEntity bullfightingPurchaseEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 实例对象
     */
    BullfightingPurchaseEntity insert(BullfightingPurchaseEntity bullfightingPurchaseEntity);

    /**
     * 修改数据
     *
     * @param bullfightingPurchaseEntity 实例对象
     * @return 实例对象
     */
    boolean update(BullfightingPurchaseEntity bullfightingPurchaseEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 查询昨日奖励池
     *
     * @param date 日期
     * @return 奖励池总额
     */
    int queryRewardPoolByGameDate(Date date);
}