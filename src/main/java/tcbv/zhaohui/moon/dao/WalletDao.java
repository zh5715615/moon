package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.WalletEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * tb_wallet(Wallet)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-03 17:02:33
 */
@Mapper
public interface WalletDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WalletEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param walletEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<WalletEntity> queryAllByLimit(@Param("entity") WalletEntity walletEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param walletEntity 查询条件
     * @return 总行数
     */
    long count(WalletEntity walletEntity);

    /**
     * 新增数据
     *
     * @param walletEntity 实例对象
     * @return 影响行数
     */
    int insert(WalletEntity walletEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<WalletEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WalletEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<WalletEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<WalletEntity> entities);

    /**
     * 修改数据
     *
     * @param walletEntity 实例对象
     * @return 影响行数
     */
    int update(WalletEntity walletEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 查询全部数据
     * @return List<WalletEntity>
     */
    List<WalletEntity> queryAll();
}

