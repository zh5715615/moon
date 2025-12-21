package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.PledgePromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 质押推广奖励表(PledgePromotion)表服务接口
 *
 * @author makejava
 * @since 2025-12-21 18:41:29
 */
public interface PledgePromotionService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PledgePromotionEntity queryById(Integer id);

    /**
     * 分页查询
     *
     * @param pledgePromotionEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PledgePromotionEntity> queryByPage(PledgePromotionEntity pledgePromotionEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 实例对象
     */
    PledgePromotionEntity insert(PledgePromotionEntity pledgePromotionEntity);

    /**
     * 修改数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 实例对象
     */
    boolean update(PledgePromotionEntity pledgePromotionEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
}
