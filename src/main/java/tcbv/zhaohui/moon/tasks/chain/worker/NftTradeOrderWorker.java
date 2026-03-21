package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.NFTTradeOrderEventBean;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.NftOrderEntity;
import tcbv.zhaohui.moon.enums.NftOrderStatusEnum;
import tcbv.zhaohui.moon.service.NftOrderService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

@Component
public class NftTradeOrderWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private NftOrderService nftOrderService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        NFTTradeOrderEventBean tradeOrderBean = dappPoolService.parseTradeOrder(params.getTxHash());
        NftOrderEntity nftOrderEntity = new NftOrderEntity();
        nftOrderEntity.setId(params.getNftOrderId());
        nftOrderEntity.setBuyerId(params.getUserId());
        nftOrderEntity.setTokenId(tradeOrderBean.getTokenId());
        nftOrderEntity.setPrice(tradeOrderBean.getPrice().doubleValue());
        nftOrderEntity.setTradeHash(params.getTxHash());
        nftOrderEntity.setStatus(NftOrderStatusEnum.TRADED.getStatus());
        nftOrderService.tradeOrder(nftOrderEntity);
    }
}
