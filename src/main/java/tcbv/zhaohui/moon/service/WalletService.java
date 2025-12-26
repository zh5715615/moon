package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.WalletEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * tb_wallet(Wallet)表服务接口
 *
 * @author makejava
 * @since 2025-12-03 15:42:53
 */
public interface WalletService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WalletEntity queryById(Integer id);

    /**
     * 分页查询
     *
     * @param walletEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<WalletEntity> queryByPage(WalletEntity walletEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param walletEntity 实例对象
     * @return 实例对象
     */
    WalletEntity insert(WalletEntity walletEntity);

    /**
     * 修改数据
     *
     * @param walletEntity 实例对象
     * @return 实例对象
     */
    boolean update(WalletEntity walletEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 查询全部数据
     * @return List<WalletEntity>
     */
    List<WalletEntity> queryAll();
}
