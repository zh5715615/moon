package tcbv.zhaohui.moon.tasks.chain.worker;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.events.PledgeEventBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.service.PledgeService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxWorker;

import java.util.Date;

@Component
public class PledgeInvokeWorker implements ChainTxWorker {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private PledgeService pledgeService;

    @Autowired
    private Web3Config web3Config;

    @Override
    public void execute(ChainTxTaskEntity task, ChainTaskParams params) throws Exception {
        PledgeEventBean pledgeEventBean = dappPoolService.parsedPledge(params.getTxHash());
        PledgeRegion pledgeRegion = pledgeEventBean.getRegion();
        Date now = new Date();
        PledgeEntity pledgeEntity = new PledgeEntity();
        pledgeEntity.setUserId(params.getUserId());
        pledgeEntity.setAddress(params.getAddress());
        pledgeEntity.setRegion(pledgeRegion.getLevel());
        pledgeEntity.setAmount(pledgeEventBean.getPledgeAmount().doubleValue());
        int expire = web3Config.isEnvProd() ? pledgeRegion.getPeriodProd() : pledgeRegion.getPeriodTest();
        pledgeEntity.setExpireTime(DateUtils.addSeconds(now, expire));
        pledgeEntity.setCreateTime(now);
        pledgeEntity.setPledgeHash(params.getTxHash());
        pledgeService.insert(pledgeEntity);
    }
}
