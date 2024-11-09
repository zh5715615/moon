package tcbv.zhaohui.moon.utils;

import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import tcbv.zhaohui.moon.beans.CandleGraphBean;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BnbPriceUtil {
    private BnbPriceUtil() {}

    public static CandleGraphBean bnbUsdtKline(long startTime, long endTime) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://api4.binance.com/api/v3/klines")
                .queryParam("symbol", "BNBUSDT")
                .queryParam("interval", "5m")
                .queryParam("startTime", String.valueOf(startTime))
                .queryParam("endTime", String.valueOf(endTime))
                .queryParam("timeZone", "0")
                .queryParam("limit", "20");

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        boolean isEnable = true;
        String proxyIp = "127.0.0.1";
        int proxyPort = 1081;
        if (isEnable) {
            requestFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort)));
        }
        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                String.class
        );

        Type dataType = new TypeToken<List<List<Object>>>() {}.getType();
        List<List<Object>> dataList = GsonUtil.fromJson(response.getBody(), dataType);
        return getCandleGraphBean(dataList);
    }

    private static long object2Long(Object obj) {
        Long longValue = null;
        if (obj instanceof Double) {
            double doubleValue = ((Double) obj).doubleValue();
            longValue = (long) doubleValue;
        } else if (obj instanceof Long) {
            longValue = (Long) obj;
        } else {
            throw new IllegalArgumentException("The object is neither a Double nor a Long.");
        }
        return longValue;
    }

    private static CandleGraphBean getCandleGraphBean(List<List<Object>> dataList) {
        CandleGraphBean candleGraphBean = null;
        if (!CollectionUtils.isEmpty(dataList)) {
            List<Object> data = dataList.get(dataList.size() - 1);
            if (!CollectionUtils.isEmpty(data) && data.size() == 12) {
                candleGraphBean = new CandleGraphBean();
                candleGraphBean.setStartTime(object2Long(data.get(0)));
                candleGraphBean.setOpenPrice(Double.parseDouble((String)data.get(1)));
                candleGraphBean.setClosePrice(Double.parseDouble((String)data.get(4)));
                candleGraphBean.setEndTime(object2Long(data.get(6)));
            }
        }
        return candleGraphBean;
    }
}
