package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * SJ套餐包交易(SjPackageTx)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-24 17:04:26
 */
@Mapper
public interface SjPackageTxDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SjPackageTxEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param sjPackageTxEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<SjPackageTxEntity> queryAllByLimit(@Param("entity") SjPackageTxEntity sjPackageTxEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param sjPackageTxEntity 查询条件
     * @return 总行数
     */
    long count(SjPackageTxEntity sjPackageTxEntity);

    /**
     * 新增数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 影响行数
     */
    int insert(SjPackageTxEntity sjPackageTxEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SjPackageTxEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SjPackageTxEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SjPackageTxEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SjPackageTxEntity> entities);

    /**
     * 修改数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 影响行数
     */
    int update(SjPackageTxEntity sjPackageTxEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过hash查询数据
     * @param hash 交易hash
     * @return 实例对象
     */
    SjPackageTxEntity queryByHash(@Param("hash") String hash);
}

