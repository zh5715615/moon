package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.entity.TbNftReward;
import tcbv.zhaohui.moon.dao.TbNftRewardDao;
import tcbv.zhaohui.moon.service.TbNftRewardService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TbNftReward)表服务实现类
 *
 * @author makejava
 * @since 2024-11-19 20:14:47
 */
@Service("tbNftRewardService")
public class TbNftRewardServiceImpl implements TbNftRewardService {
    @Resource
    private TbNftRewardDao tbNftRewardDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TbNftReward queryById(Integer id) {
        return this.tbNftRewardDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tbNftReward 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TbNftReward> queryByPage(TbNftReward tbNftReward, PageRequest pageRequest) {
        long total = this.tbNftRewardDao.count(tbNftReward);
        return new PageImpl<>(this.tbNftRewardDao.queryAllByLimit(tbNftReward, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tbNftReward 实例对象
     * @return 实例对象
     */
    @Override
    public TbNftReward insert(TbNftReward tbNftReward) {
        this.tbNftRewardDao.insert(tbNftReward);
        return tbNftReward;
    }

    /**
     * 修改数据
     *
     * @param tbNftReward 实例对象
     * @return 实例对象
     */
    @Override
    public TbNftReward update(TbNftReward tbNftReward) {
        this.tbNftRewardDao.update(tbNftReward);
        return this.queryById(tbNftReward.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tbNftRewardDao.deleteById(id) > 0;
    }

    @Override
    public void insertBatch(List<TbNftReward> nftRewardList) {
        tbNftRewardDao.insertBatch(nftRewardList);
    }

    @Override
    public void deleteByCycle(Integer nftWeekCycle) {
        tbNftRewardDao.deleteByCycle(nftWeekCycle);
    }
}
