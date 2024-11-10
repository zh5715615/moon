package tcbv.zhaohui.moon.scheduled;

import com.google.common.collect.ImmutableMap;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dawn
 * @date 2024/11/2 15:12
 */
public class TimerMaps {

    public static final String GAMEONE = "gameOne";

    public static final String GAMETWO = "gameTwo";

    private static final Map<String, LocalDateTime> timerMap = new ConcurrentHashMap<>();

    public static void setRemainingTime(String key, LocalDateTime time) {
        timerMap.put(key, time);
    }

    public static LocalDateTime getRemainingTime(String key) {
        return timerMap.get(key);
    }

    private static final Map<Integer, Boolean> gameStatus = new HashMap<>();

    static {
        gameStatus.put(1, false);
        gameStatus.put(2, false);
    }

    public static void startDiceRoller() {
        gameStatus.put(1, true);
    }

    public static void stopDiceRoller() {
        gameStatus.put(1, false);
    }

    public static boolean getDicRollerStatus() {
        return gameStatus.get(1);
    }

    public static void startGuessBnbPrice() {
        gameStatus.put(2, true);
    }

    public static void stopGuessBnbPrice() {
        gameStatus.put(2, false);
    }

    public static boolean getGuessBnbPriceStatus() {
        return gameStatus.get(2);
    }
}
