package tcbv.zhaohui.moon.dao;

import tcbv.zhaohui.moon.entity.SyslogEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 系统日志(SysLog)表数据库访问层
 *
 * @author makejava
 * @since 2025-12-19 13:36:18
 */
@Mapper
public interface SysLogDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SyslogEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param sysLogEntity 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<SyslogEntity> queryAllByLimit(@Param("entity") SyslogEntity sysLogEntity, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param sysLogEntity 查询条件
     * @return 总行数
     */
    long count(SyslogEntity sysLogEntity);

    /**
     * 新增数据
     *
     * @param sysLogEntity 实例对象
     * @return 影响行数
     */
    int insert(SyslogEntity sysLogEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysLogEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<SyslogEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<SysLogEntity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<SyslogEntity> entities);

    /**
     * 修改数据
     *
     * @param sysLogEntity 实例对象
     * @return 影响行数
     */
    int update(SyslogEntity sysLogEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);
}

