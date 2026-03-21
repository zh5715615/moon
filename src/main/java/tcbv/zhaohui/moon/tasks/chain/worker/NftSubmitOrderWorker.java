package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.SubmitOrderEventBean;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.NftOrderEntity;
import tcbv.zhaohui.moon.enums.NftOrderStatusEnum;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.NftOrderService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

import java.util.Date;

import static tcbv.zhaohui.moon.exceptions.BizException.ORDER_NOT_MATCH;

@Component
public class NftSubmitOrderWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private NftOrderService nftOrderService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        SubmitOrderEventBean submitOrderEventBean = dappPoolService.parseSubmitOrder(params.getTxHash());
        if (!params.getAddress().equalsIgnoreCase(submitOrderEventBean.getOwner())) {
            throw new BizException(ORDER_NOT_MATCH, "订单不属于当前用户");
        }
        NftOrderEntity nftOrderEntity = new NftOrderEntity();
        nftOrderEntity.setUserId(params.getUserId());
        nftOrderEntity.setAddress(params.getAddress());
        nftOrderEntity.setTokenId(submitOrderEventBean.getTokenId());
        nftOrderEntity.setPrice(submitOrderEventBean.getPrice().doubleValue());
        nftOrderEntity.setSubmitHash(params.getTxHash());
        nftOrderEntity.setStatus(NftOrderStatusEnum.PENDING.getStatus());
        nftOrderEntity.setCreateTime(new Date());
        nftOrderService.insert(nftOrderEntity);
    }
}
