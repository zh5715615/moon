package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.SyslogEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 系统日志(SysLog)表服务接口
 *
 * @author makejava
 * @since 2025-12-19 13:36:18
 */
public interface SyslogService {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    SyslogEntity queryById(Integer id);

    /**
     * 分页查询
     *
     * @param sysLogEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<SyslogEntity> queryByPage(SyslogEntity sysLogEntity, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param sysLogEntity 实例对象
     * @return 实例对象
     */
    SyslogEntity insert(SyslogEntity sysLogEntity);

    /**
     * 修改数据
     *
     * @param sysLogEntity 实例对象
     * @return 实例对象
     */
    boolean update(SyslogEntity sysLogEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);
}
