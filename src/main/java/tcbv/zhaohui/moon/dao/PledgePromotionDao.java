package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.PledgePromotionEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 质押推广奖励表(PledgePromotion)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-21 18:41:29
 */
@Mapper
public interface PledgePromotionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    PledgePromotionEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param pledgePromotionEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<PledgePromotionEntity> queryAllByLimit(@Param("entity") PledgePromotionEntity pledgePromotionEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param pledgePromotionEntity 查询条件
     * @return 总行数
     */
    long count(PledgePromotionEntity pledgePromotionEntity);

    /**
     * 新增数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 影响行数
     */
    int insert(PledgePromotionEntity pledgePromotionEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<PledgePromotionEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<PledgePromotionEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<PledgePromotionEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<PledgePromotionEntity> entities);

    /**
     * 修改数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 影响行数
     */
    int update(PledgePromotionEntity pledgePromotionEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    PledgePromotionEntity queryByIdAndPromoter(@Param("userId") String userId, @Param("pid") String pid);
}

