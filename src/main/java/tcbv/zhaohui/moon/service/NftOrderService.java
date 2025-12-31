package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.NftOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.exceptions.BizException;

/**
 * 卡片订单(NftOrder)表服务接口
 *
 * @author makejava
 * @since 2025-12-23 20:25:17
 */
public interface NftOrderService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    NftOrderEntity queryById(Integer id);

    /**
     * 分页查询
     *
     * @param nftOrderEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<NftOrderEntity> queryByPage(NftOrderEntity nftOrderEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param nftOrderEntity 实例对象
     * @return 实例对象
     */
    NftOrderEntity insert(NftOrderEntity nftOrderEntity) throws BizException;

    /**
     * 修改数据
     *
     * @param nftOrderEntity 实例对象
     * @return 实例对象
     */
    boolean update(NftOrderEntity nftOrderEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 订单成交
     * @param nftOrderEntity 订单
     */
    void tradeOrder(NftOrderEntity nftOrderEntity);

    /**
     * 订单取消
     * @param nftOrderEntity 订单
     */
    void cancelOrder(NftOrderEntity nftOrderEntity) throws Exception;
}
