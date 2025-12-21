package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.PledgePromotionEntity;
import tcbv.zhaohui.moon.dao.PledgePromotionDao;
import tcbv.zhaohui.moon.service.PledgePromotionService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 质押推广奖励表(PledgePromotion)表服务实现类
 *
 * @author makejava
 * @since 2025-12-21 18:41:29
 */
@Service("pledgePromotionService")
public class PledgePromotionServiceImpl implements PledgePromotionService {
    @Resource
    private PledgePromotionDao pledgePromotionDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public PledgePromotionEntity queryById(Integer id) {
        return this.pledgePromotionDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param pledgePromotionEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PledgePromotionEntity> queryByPage(PledgePromotionEntity pledgePromotionEntity, PageRequest pageRequest) {
        long total = this.pledgePromotionDao.count(pledgePromotionEntity);
        return new PageImpl<>(this.pledgePromotionDao.queryAllByLimit(pledgePromotionEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 实例对象
     */
    @Override
    public PledgePromotionEntity insert(PledgePromotionEntity pledgePromotionEntity) {
        this.pledgePromotionDao.insert(pledgePromotionEntity);
        return pledgePromotionEntity;
    }

    /**
     * 修改数据
     *
     * @param pledgePromotionEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(PledgePromotionEntity pledgePromotionEntity) {
        return this.pledgePromotionDao.update(pledgePromotionEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.pledgePromotionDao.deleteById(id) > 0;
    }
}
