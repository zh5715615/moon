package tcbv.zhaohui.moon.scheduled;

import tcbv.zhaohui.moon.config.MoonConstant;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dawn
 * @date 2024/11/2 15:12
 */
public class TimerMaps {
//
//    public static final String GAMEONE = "gameOne";
//
//    public static final String GAMETWO = "gameTwo";
//
//    private static final Map<String, LocalDateTime> timerMap = new ConcurrentHashMap<>();
//
//    public static void setRemainingTime(String key, LocalDateTime time) {
//        timerMap.put(key, time);
//    }
//
//    public static LocalDateTime getRemainingTime(String key) {
//        return timerMap.get(key);
//    }

    private static final Map<Integer, Boolean> gameStatus = new HashMap<>();

    static {
        gameStatus.put(MoonConstant.DICE_ROLLER_GAME, false);
        gameStatus.put(MoonConstant.GUESS_BNB_PRICE_GAME, false);
    }

    public static void startDiceRoller() {
        gameStatus.put(MoonConstant.DICE_ROLLER_GAME, true);
    }

    public static void stopDiceRoller() {
        gameStatus.put(MoonConstant.DICE_ROLLER_GAME, false);
    }

    public static boolean getDicRollerStatus() {
        return gameStatus.get(MoonConstant.DICE_ROLLER_GAME);
    }

    public static void startGuessBnbPrice() {
        gameStatus.put(MoonConstant.GUESS_BNB_PRICE_GAME, true);
    }

    public static void stopGuessBnbPrice() {
        gameStatus.put(MoonConstant.GUESS_BNB_PRICE_GAME, false);
    }

    public static boolean getGuessBnbPriceStatus() {
        return gameStatus.get(MoonConstant.GUESS_BNB_PRICE_GAME);
    }
}
