package tcbv.zhaohui.moon.dao;

import org.apache.ibatis.annotations.Mapper;
import tcbv.zhaohui.moon.entity.TbEventOption;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * (TbEventOption)表数据库访问层
 *
 * @author makejava
 * @since 2024-11-11 16:34:01
 */
@Mapper
public interface TbEventOptionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TbEventOption queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param tbEventOption 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TbEventOption> queryAllByLimit(TbEventOption tbEventOption, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tbEventOption 查询条件
     * @return 总行数
     */
    long count(TbEventOption tbEventOption);

    /**
     * 新增数据
     *
     * @param tbEventOption 实例对象
     * @return 影响行数
     */
    int insert(TbEventOption tbEventOption);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbEventOption> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbEventOption> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbEventOption> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TbEventOption> entities);

    /**
     * 修改数据
     *
     * @param tbEventOption 实例对象
     * @return 影响行数
     */
    int update(TbEventOption tbEventOption);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    List<TbEventOption> queryByEventId(String eventId);
}

