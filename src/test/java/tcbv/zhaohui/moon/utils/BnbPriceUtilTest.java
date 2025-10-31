package tcbv.zhaohui.moon.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tcbv.zhaohui.moon.beans.CandleGraphBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;



@Slf4j
public class BnbPriceUtilTest {
    @Test
    @Disabled("依赖外部行情服务，默认关闭")
    public void testBnbUsdtKline() {
        long currentTime = System.currentTimeMillis();
        long previousTime = currentTime - 60 * 60 * 1000;
        CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(previousTime, currentTime, true, "127.0.0.1", 1080);
        log.info("CandleGraphBean is {}", GsonUtil.toJson(candleGraphBean));
    }



    @Test
    public void testGetFiveMinuteRange() {
        // 测试函数
        LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 9, 22, 1); // 当前时间
        Pair<Long, Long> timeRange = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 10);
        long duration = timeRange.getRight() - timeRange.getLeft();
        Assertions.assertEquals(TimeUnit.MINUTES.toMillis(10), duration);
        Assertions.assertTrue(timeRange.getLeft() <= CustomizeTimeUtil.localDateTime2Long(localDateTime));
        Assertions.assertTrue(timeRange.getRight() > timeRange.getLeft());
    }

    @Test
    @Disabled("依赖外部行情服务，默认关闭")
    public void testBnbPrice() {
        LocalDateTime localDateTime = LocalDateTime.of(2024, 11, 9, 22, 20); // 当前时间
        Pair<Long, Long> pair = CustomizeTimeUtil.getFiveMinuteRange(localDateTime, 5);
        long startTime = pair.getLeft();
        long endTime = pair.getRight();
        log.info("Start Timestamp: {}, DateTime is {}", startTime, CustomizeTimeUtil.formatTimestamp(startTime));
        log.info("End Timestamp: {}, DateTime is {}", endTime, CustomizeTimeUtil.formatTimestamp(endTime));

        while (true) {
            CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(startTime, endTime, true, "127.0.0.1", 1081);
            LocalDateTime currentTime = LocalDateTime.now();
            long currentTimestamp = CustomizeTimeUtil.localDateTime2Long(currentTime);
            if (currentTimestamp < candleGraphBean.getEndTime()) {
                log.info("还未到开奖时间");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Assertions.fail("线程被中断", e);
                }
                continue;
            }
            log.info("CandleGraphBean is {}", GsonUtil.toJson(candleGraphBean));
            break;
        }
    }
}
