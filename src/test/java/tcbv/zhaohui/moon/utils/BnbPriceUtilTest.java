package tcbv.zhaohui.moon.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import tcbv.zhaohui.moon.beans.CandleGraphBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;



@Slf4j
public class BnbPriceUtilTest {
    @Test
    public void testBnbUsdtKline() {
        long currentTime = System.currentTimeMillis();
        long previousTime = currentTime - 60 * 60 * 1000;
        CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(previousTime, currentTime);
        log.info("CandleGraphBean is {}", GsonUtil.toJson(candleGraphBean));
    }



    @Test
    public void testGetFiveMinuteRange() {
        // 测试函数
        LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 9, 22, 1); // 当前时间
        Pair<Long, Long> timeRange = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 10);
        log.info("Start Timestamp: {}, DateTime is {}", timeRange.getLeft(), CustomizeTimeUtil.formatTimestamp(timeRange.getLeft()));
        log.info("End Timestamp: {}, DateTime is {}", timeRange.getRight(), CustomizeTimeUtil.formatTimestamp(timeRange.getRight()));
    }

    @Test
    public void testBnbPrice() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 9, 22, 20); // 当前时间
        Pair<Long, Long> pair = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 5);
        long startTime = pair.getLeft();
        long endTime = pair.getRight();
        log.info("Start Timestamp: {}, DateTime is {}", startTime, CustomizeTimeUtil.formatTimestamp(startTime));
        log.info("End Timestamp: {}, DateTime is {}", endTime, CustomizeTimeUtil.formatTimestamp(endTime));

        while (true) {
            CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(startTime, endTime);
            LocalDateTime currentTime = LocalDateTime.now();
            long currentTimestamp = CustomizeTimeUtil.localDateTime2Long(currentTime);
            if (currentTimestamp < candleGraphBean.getEndTime()) {
                log.info("还未到开奖时间");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            log.info("CandleGraphBean is {}", GsonUtil.toJson(candleGraphBean));
            break;
        }
    }
}
