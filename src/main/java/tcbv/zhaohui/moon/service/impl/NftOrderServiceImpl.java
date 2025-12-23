package tcbv.zhaohui.moon.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tcbv.zhaohui.moon.entity.NftOrderEntity;
import tcbv.zhaohui.moon.dao.NftOrderDao;
import tcbv.zhaohui.moon.enums.NftOrderStatusEnum;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.NftOrderService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

/**
 * 卡片订单(NftOrder)表服务实现类
 *
 * @author makejava
 * @since 2025-12-23 20:25:17
 */
@Service("nftOrderService")
public class NftOrderServiceImpl implements NftOrderService {
    @Resource
    private NftOrderDao nftOrderDao;

    @Autowired
    private DappPoolService dappPoolService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public NftOrderEntity queryById(Integer id) {
        return this.nftOrderDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param nftOrderEntity 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<NftOrderEntity> queryByPage(NftOrderEntity nftOrderEntity, PageRequest pageRequest) {
        long total = this.nftOrderDao.count(nftOrderEntity);
        return new PageImpl<>(this.nftOrderDao.queryAllByLimit(nftOrderEntity, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param nftOrderEntity 实例对象
     * @return 实例对象
     */
    @Override
    public NftOrderEntity insert(NftOrderEntity nftOrderEntity) throws Exception {
        String submitHash = nftOrderEntity.getSubmitHash();
        NftOrderEntity queryEntity = this.nftOrderDao.queryBySubmitHash(submitHash);
        if (queryEntity != null) {
            throw new RuntimeException("SubmitHash[" + submitHash + "]对应订单已存在");
        }
        nftOrderEntity.setCreateTime(new Date());
        BigInteger tokenId = new BigInteger(nftOrderEntity.getTokenId());
        submitHash = dappPoolService.submitOrder(nftOrderEntity.getAddress(), tokenId, nftOrderEntity.getPrice());
        nftOrderEntity.setSubmitHash(submitHash);
        this.nftOrderDao.insert(nftOrderEntity);
        return nftOrderEntity;
    }

    /**
     * 修改数据
     *
     * @param nftOrderEntity 实例对象
     * @return 实例对象
     */
    @Override
    public boolean update(NftOrderEntity nftOrderEntity) {
        return this.nftOrderDao.update(nftOrderEntity) > 0;
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.nftOrderDao.deleteById(id) > 0;
    }

    @Override
    public void tradeOrder(NftOrderEntity nftOrderEntity) {
        NftOrderEntity queryEntity = this.nftOrderDao.queryById(nftOrderEntity.getId());
        if (StringUtils.isNotBlank(queryEntity.getTradeHash()) &&
                queryEntity.getTradeHash().equals(nftOrderEntity.getTradeHash())) {
            throw new RuntimeException("TraderHash[" + nftOrderEntity.getTradeHash() + "]对应订单已存在");
        }
        if (!queryEntity.getUserId().equals(nftOrderEntity.getBuyerId())) {
            throw new RuntimeException("订单不属于该用户");
        }
        if (!queryEntity.getTokenId().equals(nftOrderEntity.getTokenId())) {
            throw new RuntimeException("NFT tokenId不属于该订单");
        }
        if (!queryEntity.getPrice().equals(nftOrderEntity.getPrice())) {
            throw new RuntimeException("订单价格不匹配");
        }
        if (!queryEntity.getStatus().equals(NftOrderStatusEnum.PENDING.getStatus())) {
            throw new RuntimeException("订单状态不正确，不是挂单状态");
        }
        nftOrderEntity.setTradeTime(new Date());
        this.nftOrderDao.update(nftOrderEntity);
    }
}
