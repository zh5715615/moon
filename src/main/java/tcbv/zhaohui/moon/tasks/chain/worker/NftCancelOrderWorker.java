package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.CancelOrderEventBean;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.NftOrderEntity;
import tcbv.zhaohui.moon.enums.NftOrderStatusEnum;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.NftOrderService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

import static tcbv.zhaohui.moon.exceptions.BizException.ORDER_NOT_MATCH;

@Component
public class NftCancelOrderWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private NftOrderService nftOrderService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        CancelOrderEventBean cancelOrderEventBean = dappPoolService.parseCancelOrder(params.getTxHash());
        if (!params.getAddress().equalsIgnoreCase(cancelOrderEventBean.getOwner())) {
            throw new BizException(ORDER_NOT_MATCH, "订单不属于当前用户");
        }
        NftOrderEntity nftOrderEntity = new NftOrderEntity();
        nftOrderEntity.setId(params.getNftOrderId());
        nftOrderEntity.setUserId(params.getUserId());
        nftOrderEntity.setTokenId(cancelOrderEventBean.getTokenId());
        nftOrderEntity.setCancelHash(params.getTxHash());
        nftOrderEntity.setStatus(NftOrderStatusEnum.CANCEL.getStatus());
        nftOrderEntity.setAddress(params.getAddress());
        nftOrderService.cancelOrder(nftOrderEntity);
    }
}
