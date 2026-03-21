package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.entity.BullfightingPurchaseEntity;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.service.BullfightingPurchaseService;
import tcbv.zhaohui.moon.service.chain.BullfightingGameSampleService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

@Component
public class BullfightingBuyTimesWorker implements ChainTxWorker {

    @Autowired
    private BullfightingGameSampleService gameSampleService;

    @Autowired
    private BullfightingPurchaseService bullfightingPurchaseService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        BuyGameTimesEventBean event = gameSampleService.parseBuyGameTimes(params.getTxHash());
        BullfightingPurchaseEntity entity = new BullfightingPurchaseEntity();
        entity.setAddress(params.getAddress());
        entity.setUserId(params.getUserId());
        entity.setTimes(event.getTimes());
        entity.setAmount(event.getAmount().doubleValue());
        entity.setHash(params.getTxHash());
        bullfightingPurchaseService.insert(entity);
    }
}
