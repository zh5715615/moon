package tcbv.zhaohui.moon.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import tcbv.zhaohui.moon.beans.CandleGraphBean;

@Slf4j
public class BnbPriceUtilTest {
    @Test
    public void testBnbUsdtKline() {
        long currentTime = System.currentTimeMillis();
        long previousTime = currentTime - 60 * 60 * 1000;
        CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(previousTime, currentTime);
        log.info("CandleGraphBean is {}", GsonUtil.toJson(candleGraphBean));
    }
}
