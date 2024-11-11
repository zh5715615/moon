package tcbv.zhaohui.moon.dao;

import org.apache.ibatis.annotations.Mapper;
import tcbv.zhaohui.moon.entity.TbEvent;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (TbEvent)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-11 17:06:50
 */
@Mapper
public interface TbEventDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbEvent queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param tbEvent 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TbEvent> queryAllByLimit(TbEvent tbEvent, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tbEvent 查询条件
     * @return 总行数
     */
    long count(TbEvent tbEvent);

    /**
     * 新增数据
     *
     * @param tbEvent 实例对象
     * @return 影响行数
     */
    int insert(TbEvent tbEvent);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbEvent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbEvent> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbEvent> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TbEvent> entities);

    /**
     * 修改数据
     *
     * @param tbEvent 实例对象
     * @return 影响行数
     */
    int update(TbEvent tbEvent);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}

