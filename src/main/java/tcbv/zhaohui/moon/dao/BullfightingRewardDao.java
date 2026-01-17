package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.BullfightingRewardEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 斗牛游戏领取奖励记录(BullfightingReward)表数据库访问层
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Mapper
public interface BullfightingRewardDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    BullfightingRewardEntity queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param bullfightingRewardEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<BullfightingRewardEntity> queryAllByLimit(@Param("entity") BullfightingRewardEntity bullfightingRewardEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param bullfightingRewardEntity 查询条件
     * @return 总行数
     */
    long count(BullfightingRewardEntity bullfightingRewardEntity);

    /**
     * 新增数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 影响行数
     */
    int insert(BullfightingRewardEntity bullfightingRewardEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingRewardEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<BullfightingRewardEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<BullfightingRewardEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<BullfightingRewardEntity> entities);

    /**
     * 修改数据
     *
     * @param bullfightingRewardEntity 实例对象
     * @return 影响行数
     */
    int update(BullfightingRewardEntity bullfightingRewardEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);
}

