package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.BuySpaceJediPackageEventBean;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import tcbv.zhaohui.moon.service.SjPackageTxService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

@Component
public class SjBuyPackageWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private SjPackageTxService sjPackageTxService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        BuySpaceJediPackageEventBean event = dappPoolService.parseBuySpaceJediPackage(params.getTxHash());
        SjPackageTxEntity entity = new SjPackageTxEntity();
        entity.setBuyerId(params.getUserId());
        entity.setAddress(params.getAddress());
        entity.setHash(params.getTxHash());
        entity.setPacakgeCnt(event.getBuyCnt());
        entity.setPrice(event.getPrice().doubleValue());
        entity.setStage(event.getStage());
        sjPackageTxService.insert(entity);
    }
}
