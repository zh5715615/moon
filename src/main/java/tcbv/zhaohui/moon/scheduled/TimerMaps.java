package tcbv.zhaohui.moon.scheduled;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dawn
 * @date 2024/11/2 15:12
 */
public class TimerMaps {
    private static final Map<String, LocalDateTime> timerMap = new ConcurrentHashMap<>();

    public static void setRemainingTime(String key, LocalDateTime time) {
        timerMap.put(key, time);
    }

    public static LocalDateTime getRemainingTime(String key) {
        return timerMap.get(key);
    }
}
