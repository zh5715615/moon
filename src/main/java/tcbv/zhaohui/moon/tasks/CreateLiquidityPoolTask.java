package tcbv.zhaohui.moon.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.PresaleInfoBean;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.chain.Token20Service;

import java.util.Date;

import static tcbv.zhaohui.moon.beans.Constants.PRESALE_TOTAL;
import static tcbv.zhaohui.moon.exceptions.ChainException.INVOKE_EXCEPTION;

/**
 * @author: zhaohui
 * @Title: CreateLiquidityPoolTask
 * @Description:
 * @date: 2026/1/15 21:44
 */
@Component
@Slf4j
public class CreateLiquidityPoolTask {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

//    @Scheduled(cron = "0 0/10 * * * ?")
    public void hourlyTask() {
        log.info("当前时间：{}，执行每十分钟任务。", new Date());
        long currentSec = System.currentTimeMillis() / 1000;
        try {
            long presaleTime = dappPoolService.getPresaleTime();
            PresaleInfoBean presaleInfoBean = dappPoolService.getPackageCnt();
            if (currentSec > presaleTime || presaleInfoBean.getSold() >= PRESALE_TOTAL) {
                spaceJediService.enableLiquidityCreation();
                String txHash = dappPoolService.createLiquidityPool();
                log.info("创建流动性池，txHash = {}", txHash);
            }
        } catch (Exception e) {
            throw new ChainException(INVOKE_EXCEPTION, e.getMessage());
        }
    }
}
