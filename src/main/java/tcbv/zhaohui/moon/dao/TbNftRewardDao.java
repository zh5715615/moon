package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.TbNftReward;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * (TbNftReward)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-19 20:14:47
 */
 @Mapper
public interface TbNftRewardDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbNftReward queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tbNftReward 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TbNftReward> queryAllByLimit(@Param("entity") TbNftReward tbNftReward, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tbNftReward 查询条件
     * @return 总行数
     */
    long count(TbNftReward tbNftReward);

    /**
     * 新增数据
     *
     * @param tbNftReward 实例对象
     * @return 影响行数
     */
    int insert(TbNftReward tbNftReward);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbNftReward> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbNftReward> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbNftReward> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TbNftReward> entities);

    /**
     * 修改数据
     *
     * @param tbNftReward 实例对象
     * @return 影响行数
     */
    int update(TbNftReward tbNftReward);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 根据奖池周期删除排名数据
     * @param cycle
     */
    void deleteByCycle(Integer cycle);
}

