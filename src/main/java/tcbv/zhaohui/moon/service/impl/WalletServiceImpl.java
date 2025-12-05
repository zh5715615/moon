package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.WalletEntity;
import tcbv.zhaohui.moon.dao.WalletDao;
import tcbv.zhaohui.moon.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * tb_wallet(Wallet)表服务实现类
 *
 * @author makejava
 * @since 2025-12-03 15:16:10
 */
@Service("walletService")
public class WalletServiceImpl implements WalletService {
    @Resource
    private WalletDao walletDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public WalletEntity queryById(Integer id) {
        return this.walletDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param walletEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<WalletEntity> queryByPage(WalletEntity walletEntity, PageRequest pageRequest) {
        long total = this.walletDao.count(walletEntity);
        return new PageImpl<>(this.walletDao.queryAllByLimit(walletEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param walletEntity 实例对象
     * @return 实例对象
     */
    @Override
    public WalletEntity insert(WalletEntity walletEntity) {
        walletEntity.setCreateTime(new Date());
        this.walletDao.insert(walletEntity);
        return walletEntity;
    }

    /**
     * 修改数据
     *
     * @param walletEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(WalletEntity walletEntity) {
        return this.walletDao.update(walletEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.walletDao.deleteById(id) > 0;
    }
}
