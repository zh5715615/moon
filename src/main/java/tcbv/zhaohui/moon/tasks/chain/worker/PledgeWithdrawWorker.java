package tcbv.zhaohui.moon.tasks.chain.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.WithdrawEventBean;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.service.PledgeService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

@Component
public class PledgeWithdrawWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private PledgeService pledgeService;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        WithdrawEventBean withdrawEventBean = dappPoolService.parsedWithdraw(params.getTxHash());
        PledgeRegion pledgeRegion = withdrawEventBean.getRegion();
        PledgeEntity pledgeEntity = new PledgeEntity();
        pledgeEntity.setUserId(params.getUserId());
        pledgeEntity.setAddress(params.getAddress());
        pledgeEntity.setRegion(pledgeRegion.getLevel());
        pledgeEntity.setWithdrawAmount(withdrawEventBean.getWithrawAmount().doubleValue());
        pledgeEntity.setId(params.getPledgeId());
        pledgeEntity.setWithdrawHash(params.getTxHash());
        pledgeService.withdraw(pledgeEntity);
    }
}
