package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.TbNftReward;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * (TbNftReward)表服务接口
 *
 * @author makejava
 * @since 2024-11-19 20:14:47
 */
public interface TbNftRewardService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbNftReward queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tbNftReward 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TbNftReward> queryByPage(TbNftReward tbNftReward, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tbNftReward 实例对象
     * @return 实例对象
     */
    TbNftReward insert(TbNftReward tbNftReward);

    /**
     * 修改数据
     *
     * @param tbNftReward 实例对象
     * @return 实例对象
     */
    TbNftReward update(TbNftReward tbNftReward);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param nftRewardList List<TbNftReward> 实例对象列表
     * @return 影响行数
     */
    void insertBatch(List<TbNftReward> nftRewardList);

    /**
     * 根据奖池周期删除排名数据
     * @param cycle
     */
    void deleteByCycle(Integer cycle);
}
