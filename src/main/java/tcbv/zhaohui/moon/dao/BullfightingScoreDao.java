package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import tcbv.zhaohui.moon.vo.BullfightingUserStatisticsVo;

import java.util.Date;
import java.util.List;

/**
 * 斗牛用户积分(BullfightingScore)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
@Mapper
public interface BullfightingScoreDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingScoreEntity queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param bullfightingScoreEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<BullfightingScoreEntity> queryAllByLimit(@Param("entity") BullfightingScoreEntity bullfightingScoreEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param bullfightingScoreEntity 查询条件
     * @return 总行数
     */
    long count(BullfightingScoreEntity bullfightingScoreEntity);

    /**
     * 新增数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 影响行数
     */
    int insert(BullfightingScoreEntity bullfightingScoreEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingScoreEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BullfightingScoreEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingScoreEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BullfightingScoreEntity> entities);

    /**
     * 修改数据
     *
     * @param bullfightingScoreEntity 实例对象
     * @return 影响行数
     */
    int update(BullfightingScoreEntity bullfightingScoreEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    /**
     * 根据用户id查询积分记录
     * @param userId 用户id
     * @param gameDate 游戏日期
     * @return 返回值
     */
    BullfightingScoreEntity queryByUserId(@Param("userId") String userId, @Param("gameDate")  Date gameDate);

    /**
     * 用户统计
     * @param userId 用户id
     * @return 用户统计信息
     */
    BullfightingUserStatisticsVo statictics(@Param("userId") String userId, @Param("gameDate") Date gameDate);
}

