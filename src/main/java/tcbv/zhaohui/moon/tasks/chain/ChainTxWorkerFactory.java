package tcbv.zhaohui.moon.tasks.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.enums.ChainTxBizType;
import tcbv.zhaohui.moon.tasks.chain.worker.*;

@Component
public class ChainTxWorkerFactory {

    @Autowired private PledgeInvokeWorker pledgeInvokeWorker;
    @Autowired private PledgeWithdrawWorker pledgeWithdrawWorker;
    @Autowired private NftSubmitOrderWorker nftSubmitOrderWorker;
    @Autowired private NftTradeOrderWorker nftTradeOrderWorker;
    @Autowired private NftCancelOrderWorker nftCancelOrderWorker;
    @Autowired private SjBuyPackageWorker sjBuyPackageWorker;
    @Autowired private BullfightingBuyTimesWorker bullfightingBuyTimesWorker;

    public ChainTxWorker getWorker(String bizType) {
        ChainTxBizType type = ChainTxBizType.valueOf(bizType);
        switch (type) {
            case PLEDGE_INVOKE:           return pledgeInvokeWorker;
            case PLEDGE_WITHDRAW:         return pledgeWithdrawWorker;
            case NFT_SUBMIT_ORDER:        return nftSubmitOrderWorker;
            case NFT_TRADE_ORDER:         return nftTradeOrderWorker;
            case NFT_CANCEL_ORDER:        return nftCancelOrderWorker;
            case SJ_BUY_PACKAGE:          return sjBuyPackageWorker;
            case BULLFIGHTING_BUY_TIMES:  return bullfightingBuyTimesWorker;
            default: throw new IllegalArgumentException("Unknown biz_type: " + bizType);
        }
    }
}
