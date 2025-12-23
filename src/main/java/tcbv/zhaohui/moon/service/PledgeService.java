package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.PledgeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 质押表(Pledge)表服务接口
 *
 * @author makejava
 * @since 2025-12-21 11:01:17
 */
public interface PledgeService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PledgeEntity queryById(String id);

    /**
     * 分页查询
     *
     * @param pledgeEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PledgeEntity> queryByPage(PledgeEntity pledgeEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param pledgeEntity 实例对象
     * @return 实例对象
     */
    PledgeEntity insert(PledgeEntity pledgeEntity);

    /**
     * 修改数据
     *
     * @param pledgeEntity 实例对象
     * @return 实例对象
     */
    boolean update(PledgeEntity pledgeEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

    /**
     * 提取质押奖励
     * @param pledgeEntity 质押信息
     * @return 质押信息
     */
    PledgeEntity withdraw(PledgeEntity pledgeEntity);
}
