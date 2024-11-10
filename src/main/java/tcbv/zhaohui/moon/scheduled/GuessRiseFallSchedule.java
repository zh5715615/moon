package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.CandleGraphBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.BnbPriceUtil;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@Slf4j
public class GuessRiseFallSchedule extends AllocReward {

    @Autowired
    private Web3Config web3Config;

    @Resource
    private TbTxRecordDao txRecordDao;

    @Resource
    private TbGameResultDao gameResultDao;

    @Resource
    private TbUserDao userDao;

    @Autowired
    private IMoonBaseService moonBaseService;

    @Scheduled(cron = "0 0/5 * * * ? ")
    public void startScheduleOn() {
        Integer gameTurns = gameResultDao.maxTurns(2);
        if (gameTurns == null) {
            gameTurns = 0;
        }
        gameTurns++;
        log.info("第{}轮猜BNB涨跌投注时间，可以下注.", gameTurns);
        TbGameResult gameResult = new TbGameResult();
        gameResult.setId(UUID.randomUUID().toString());
        gameResult.setGameType(2);
        gameResult.setTurns(gameTurns);
        gameResultDao.insert(gameResult);
    }

    @Scheduled(cron = "0 4/5 * * * ? ")
    public void startScheduleOff() {
        log.info("猜BNB涨跌投注时间，不能下注.");
        LocalDateTime localDateTime = LocalDateTime.now(); // 当前时间
        Pair<Long, Long> pair = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 5);
        long startTime = pair.getLeft();
        long endTime = pair.getRight() - 1;
        log.info("Start Timestamp: {}, DateTime is {}", startTime, CustomizeTimeUtil.formatTimestamp(startTime));
        log.info("End Timestamp: {}, DateTime is {}", endTime, CustomizeTimeUtil.formatTimestamp(endTime));

        while (true) {
            CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(startTime, endTime,
                    web3Config.isProxy(), web3Config.getHostname(), web3Config.getPort());
            LocalDateTime currentTime = LocalDateTime.now();
            long currentTimestamp = CustomizeTimeUtil.localDateTime2Long(currentTime);
            if (currentTimestamp < candleGraphBean.getEndTime()) {
                log.debug("还未到开奖时间");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            boolean rise = candleGraphBean.getClosePrice() > candleGraphBean.getOpenPrice();
            log.info("当前涨跌情况：{}", rise ? "涨" : "跌");

            Integer gameTurns = gameResultDao.maxTurns(2);
            if (gameTurns == null) {
                return;
            }
            TbGameResult gameResult = gameResultDao.findGameTypeAndTurnsInfo(gameTurns, 2);
            if (gameResult == null) {
                return;
            }
            gameResult.setRaseAndFall(rise ? 1 : 2);
            gameResult.setDrawnTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
            gameResultDao.update(gameResult);

            allocReward(moonBaseService, userDao, txRecordDao, gameTurns, rise ? 1 : 2);
            break;
        }
    }
}
