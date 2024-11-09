package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.beans.CandleGraphBean;
import tcbv.zhaohui.moon.utils.BnbPriceUtil;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.time.LocalDateTime;

@Component
@Slf4j
public class GuessRiseFallSchedule {
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void startScheduleOn() {
        log.info("猜BNB涨跌投注时间，可以下注.");
    }

    @Scheduled(cron = "0 4/5 * * * ? ")
    public void startScheduleOff() {
        log.info("猜BNB涨跌投注时间，不能下注.");
        LocalDateTime localDateTime = LocalDateTime.now(); // 当前时间
        Pair<Long, Long> pair = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 5);
        long startTime = pair.getLeft();
        long endTime = pair.getRight();
        log.info("Start Timestamp: {}, DateTime is {}", startTime, CustomizeTimeUtil.formatTimestamp(startTime));
        log.info("End Timestamp: {}, DateTime is {}", endTime, CustomizeTimeUtil.formatTimestamp(endTime));

        while (true) {
            CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(startTime, endTime);
            LocalDateTime currentTime = LocalDateTime.now();
            long currentTimestamp = CustomizeTimeUtil.localDateTime2Long(currentTime);
            log.info("currentTimestamp = {}, candleGraphBean.getEndTime() = {}", currentTimestamp, candleGraphBean.getEndTime());
            if (currentTimestamp < candleGraphBean.getEndTime()) {
                log.debug("还未到开奖时间");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            log.info("当前涨跌情况：{}", candleGraphBean.getClosePrice() > candleGraphBean.getOpenPrice() ? "涨" : "跌");
            break;
        }
    }
}
