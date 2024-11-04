package tcbv.zhaohui.moon.scheduled;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dawn
 * @date 2024/11/2 15:12
 */
public class TimerMaps {
    private static final Map<String, Long> timerMap = new ConcurrentHashMap<>();

    public static void setRemainingTime(String key, long time) {
        timerMap.put(key, time);
    }

    public static Long getRemainingTime(String key) {
        return timerMap.get(key);
    }
}
