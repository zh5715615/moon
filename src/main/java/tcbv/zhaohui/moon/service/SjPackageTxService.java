package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * SJ套餐包交易(SjPackageTx)表服务接口
 *
 * @author makejava
 * @since 2025-12-24 17:04:26
 */
public interface SjPackageTxService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SjPackageTxEntity queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sjPackageTxEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<SjPackageTxEntity> queryByPage(SjPackageTxEntity sjPackageTxEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 实例对象
     */
    SjPackageTxEntity insert(SjPackageTxEntity sjPackageTxEntity);

    /**
     * 修改数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 实例对象
     */
    boolean update(SjPackageTxEntity sjPackageTxEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
}
