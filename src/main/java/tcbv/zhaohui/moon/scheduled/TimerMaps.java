package tcbv.zhaohui.moon.scheduled;

import lombok.Getter;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.vo.NFTRankVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dawn
 * @date 2024/11/2 15:12
 */
public class TimerMaps {
    private static final Map<Integer, Boolean> gameStatus = new HashMap<>();

    @Getter
    private static final List<NFTRankVo> nftRankVoList = new ArrayList<>();

    static {
        gameStatus.put(MoonConstant.DICE_ROLLER_GAME, false);
        gameStatus.put(MoonConstant.GUESS_BNB_PRICE_GAME, false);
        gameStatus.put(MoonConstant.GUESS_EVENT_GAME, false);
    }

    public static void addNFTRankVo(NFTRankVo nftRankVo) {
        nftRankVoList.add(nftRankVo);
    }

    public static void clearNFTRankVoList() {
        nftRankVoList.clear();
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

    public static void startGuessEvent() {
        gameStatus.put(MoonConstant.GUESS_EVENT_GAME, true);
    }

    public static void stopGuessEvent() {
        gameStatus.put(MoonConstant.GUESS_EVENT_GAME, false);
    }

    public static boolean getGuessEventStatus() {
        return gameStatus.get(MoonConstant.GUESS_EVENT_GAME);
    }
}
