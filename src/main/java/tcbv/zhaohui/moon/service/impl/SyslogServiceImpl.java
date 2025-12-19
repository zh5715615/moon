package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.SyslogEntity;
import tcbv.zhaohui.moon.dao.SysLogDao;
import tcbv.zhaohui.moon.service.SyslogService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 系统日志(SysLog)表服务实现类
 *
 * @author makejava
 * @since 2025-12-19 13:36:18
 */
@Service("sysLogService")
public class SyslogServiceImpl implements SyslogService {
    @Resource
    private SysLogDao sysLogDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SyslogEntity queryById(Integer id) {
        return this.sysLogDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sysLogEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<SyslogEntity> queryByPage(SyslogEntity sysLogEntity, PageRequest pageRequest) {
        long total = this.sysLogDao.count(sysLogEntity);
        return new PageImpl<>(this.sysLogDao.queryAllByLimit(sysLogEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param sysLogEntity 实例对象
     * @return 实例对象
     */
    @Override
    public SyslogEntity insert(SyslogEntity sysLogEntity) {
        this.sysLogDao.insert(sysLogEntity);
        return sysLogEntity;
    }

    /**
     * 修改数据
     *
     * @param sysLogEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(SyslogEntity sysLogEntity) {
        return this.sysLogDao.update(sysLogEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sysLogDao.deleteById(id) > 0;
    }
}
