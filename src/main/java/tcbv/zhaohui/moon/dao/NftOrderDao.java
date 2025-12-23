package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.NftOrderEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 卡片订单(NftOrder)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-23 20:25:16
 */
@Mapper
public interface NftOrderDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    NftOrderEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param nftOrderEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<NftOrderEntity> queryAllByLimit(@Param("entity") NftOrderEntity nftOrderEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param nftOrderEntity 查询条件
     * @return 总行数
     */
    long count(NftOrderEntity nftOrderEntity);

    /**
     * 新增数据
     *
     * @param nftOrderEntity 实例对象
     * @return 影响行数
     */
    int insert(NftOrderEntity nftOrderEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<NftOrderEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<NftOrderEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<NftOrderEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<NftOrderEntity> entities);

    /**
     * 修改数据
     *
     * @param nftOrderEntity 实例对象
     * @return 影响行数
     */
    int update(NftOrderEntity nftOrderEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过submitHash查询数据
     * @param submitHash 交易hash
     * @return 实例对象
     */
    NftOrderEntity queryBySubmitHash(@Param("submitHash") String submitHash);
}

