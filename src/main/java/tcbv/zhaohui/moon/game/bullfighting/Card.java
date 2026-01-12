package tcbv.zhaohui.moon.game.bullfighting;

/**
 * @author: zhaohui
 * @Title: Card
 * @Description:
 * @date: 2026/1/12 21:13
 */
public class Card {
    private final String suit; // 花色：♠, ♥, ♦, ♣

    private final String rank; // 点数：A, 2, ..., K

    private final int value;   // 数值：A=1, 2-10=对应数字, J/Q/K=10

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = calculateValue(rank);
    }

    private int calculateValue(String rank) {
        return switch (rank) {
            case "A" -> 1;
            case "J", "Q", "K" -> 10;
            default -> Integer.parseInt(rank);
        };
    }

    public String getSuit() { return suit; }

    public String getRank() { return rank; }

    public int getValue() { return value; }

    @Override
    public String toString() {
        return suit + rank;
    }

    // 可选：用于调试或日志
    public String toDetailedString() {
        return String.format("%s (值=%d)", toString(), value);
    }
}
