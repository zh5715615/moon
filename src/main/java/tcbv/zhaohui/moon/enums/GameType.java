package tcbv.zhaohui.moon.enums;

/**
 * @author zhaohui
 * 游戏类型
 */
public enum GameType {
    SHAKE_THE_SIEVE(1), //摇筛子
    GUESS_THE_RISE_AND_FAIL(2), //猜涨跌
    GUESSING_EVENTS(3); //猜事件
    final int code;

    GameType(int code) {
        this.code = code;
    }
}
