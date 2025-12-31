package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import tcbv.zhaohui.moon.dao.SjPackageTxDao;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.SjPackageTxService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.BizException.HASH_ALREADY_HANDLE;

/**
 * SJ套餐包交易(SjPackageTx)表服务实现类
 *
 * @author makejava
 * @since 2025-12-24 17:04:26
 */
@Service("sjPackageTxService")
public class SjPackageTxServiceImpl implements SjPackageTxService {
    @Resource
    private SjPackageTxDao sjPackageTxDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public SjPackageTxEntity queryById(Integer id) {
        return this.sjPackageTxDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param sjPackageTxEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<SjPackageTxEntity> queryByPage(SjPackageTxEntity sjPackageTxEntity, PageRequest pageRequest) {
        long total = this.sjPackageTxDao.count(sjPackageTxEntity);
        return new PageImpl<>(this.sjPackageTxDao.queryAllByLimit(sjPackageTxEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 实例对象
     */
    @Override
    public SjPackageTxEntity insert(SjPackageTxEntity sjPackageTxEntity) {
        SjPackageTxEntity queryEntity = this.sjPackageTxDao.queryByHash(sjPackageTxEntity.getHash());
        if (queryEntity != null) {
            throw new BizException(HASH_ALREADY_HANDLE, "TraderHash[" + sjPackageTxEntity.getHash() + "]对应订单已存在");
        }
        sjPackageTxEntity.setCreateTime(new Date());
        this.sjPackageTxDao.insert(sjPackageTxEntity);
        return sjPackageTxEntity;
    }

    /**
     * 修改数据
     *
     * @param sjPackageTxEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(SjPackageTxEntity sjPackageTxEntity) {
        return this.sjPackageTxDao.update(sjPackageTxEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.sjPackageTxDao.deleteById(id) > 0;
    }
}
