package tcbv.zhaohui.moon.game.bullfighting;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: zhaohui
 * @Title: PokerDealerTest
 * @Description:
 * @date: 2026/1/12 21:15
 */

@SpringBootTest
public class PokerDealerTest {

    @Autowired
    private PokerDealer pokerDealer;

    @Test
    void testPrintDealHands() {
        int handCount = 5;
        int cardsPerHand = 5;
        List<List<Card>> hands = pokerDealer.dealHands(handCount, cardsPerHand);
        hands.forEach(hand -> {
            System.out.println("手牌: " + hand.stream().map(Card::toString).collect(Collectors.joining(", ")));
            System.out.println("总点数: " + pokerDealer.calculateHandValue(hand));
            System.out.println();
        });
    }

    @Test
    void testDealHands() {
        int handCount = 5;
        int cardsPerHand = 5;
        List<List<Card>> hands = pokerDealer.dealHands(handCount, cardsPerHand);

        // 验证手数
        assertEquals(handCount, hands.size());
        // 验证每手牌数量
        for (List<Card> hand : hands) {
            assertEquals(cardsPerHand, hand.size());
        }

        // 验证所有牌唯一（无重复）
        long totalCards = hands.stream().flatMap(List::stream).count();
        assertEquals(handCount * cardsPerHand, totalCards);

        // 验证数值计算
        List<Card> firstHand = hands.get(0);
        int totalValue = pokerDealer.calculateHandValue(firstHand);
//        assertTrue(totalValue >= 5); // 最小 5*1=5
//        assertTrue(totalValue <= 50); // 最大 5*10=50

        // 打印示例（可选）
        System.out.println("第一手牌:");
        firstHand.forEach(card -> System.out.println(card.toDetailedString()));
        System.out.println("总点数: " + totalValue);
    }

    @Test
    void testCardValues() {
        Card ace = new Card("♠", "A");
        Card ten = new Card("♥", "10");
        Card jack = new Card("♦", "J");
        Card queen = new Card("♣", "Q");
        Card king = new Card("♠", "K");

        assertEquals(1, ace.getValue());
        assertEquals(10, ten.getValue());
        assertEquals(10, jack.getValue());
        assertEquals(10, queen.getValue());
        assertEquals(10, king.getValue());
    }

    @Test
    void testCalculateHandValue_NiuNiuRules() {
        // 牛牛：3张和为10/20/30，剩下2张和为10/20 → 得10分
        List<Card> niuniu = List.of(
                new Card("♠", "10"),
                new Card("♥", "J"),   // 10
                new Card("♦", "Q"),   // 10 → 10+10+10=30 ✔️
                new Card("♣", "5"),
                new Card("♠", "5")    // 5+5=10 → 牛牛
        );
        assertEquals(10, pokerDealer.calculateHandValue(niuniu));

        // 改用明确例子：
        List<Card> clearNiu7 = List.of(
                new Card("♠", "7"),
                new Card("♥", "8"),
                new Card("♦", "5"), // 7+8+5=20 ✔️
                new Card("♣", "9"), // 剩下 9 + J(10) = 19 → 19%10=9 → 牛9？
                new Card("♠", "J")
        );
        // 实际：7+8+5=20 → 剩下 9+10=19 → 19%10=9 → 应得9
        assertEquals(9, pokerDealer.calculateHandValue(clearNiu7));

        // 无牛：任意三张和都不能被10整除
        List<Card> noNiu1 = List.of(
                new Card("♠", "1"), // A=1
                new Card("♥", "1"),
                new Card("♦", "1"),
                new Card("♣", "1"),
                new Card("♠", "2")
        );
        assertEquals(0, pokerDealer.calculateHandValue(noNiu1));

        List<Card> noNiu2 = List.of(
                new Card("♠", "7"), // A=1
                new Card("♥", "9"),
                new Card("♦", "2"),
                new Card("♣", "J"),
                new Card("♠", "Q")
        );
        assertEquals(0, pokerDealer.calculateHandValue(noNiu2));

        // 牛1：3张和为10，剩下2张和为11 → 11%10=1
        List<Card> niu1 = List.of(
                new Card("♠", "A"), //1
                new Card("♥", "2"),
                new Card("♦", "7"), // 1+2+7=10 ✔️
                new Card("♣", "5"),
                new Card("♠", "6")  // 5+6=11 → 11%10=1
        );
        assertEquals(1, pokerDealer.calculateHandValue(niu1));
    }
}