package tcbv.zhaohui.moon.game.bullfighting;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author: zhaohui
 * @Title: DealPoker
 * @Description:
 * @date: 2026/1/12 21:09
 */
@Component
public class PokerDealer {

    private static final String[] SUITS = {"♠", "♥", "♦", "♣"};

    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    // 在 PokerDealer 类中添加一个静态常量（只初始化一次）
    private static final int[][] THREE_CARD_COMBINATIONS = {
            {0, 1, 2}, {0, 1, 3}, {0, 1, 4},
            {0, 2, 3}, {0, 2, 4},
            {0, 3, 4},
            {1, 2, 3}, {1, 2, 4},
            {1, 3, 4},
            {2, 3, 4}
    };

    /**
     * 创建一副52张的新牌（不含大小王）
     */
    public List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(suit, rank));
            }
        }
        return deck;
    }

    /**
     * 洗牌并随机发5手牌，每手5张
     * @return List<List<Card>> 5个玩家的手牌
     */
    public List<List<Card>> dealHands(int handCount, int cardsPerHand) {
        List<Card> deck = createDeck();
        Collections.shuffle(deck, new Random());

        if (handCount * cardsPerHand > deck.size()) {
            throw new IllegalArgumentException("牌不够发！");
        }

        List<List<Card>> hands = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < handCount; i++) {
            List<Card> hand = new ArrayList<>();
            for (int j = 0; j < cardsPerHand; j++) {
                hand.add(deck.get(index++));
            }
            hands.add(hand);
        }
        return hands;
    }

    /**
     * 使用预定义三元组优化后的“牛牛”计算逻辑
     */
    public int calculateHandValue(List<Card> hand) {
        if (hand == null || hand.size() != 5) {
            throw new IllegalArgumentException("必须是5张牌");
        }

        int[] values = hand.stream().mapToInt(Card::getValue).toArray();

        for (int[] triple : THREE_CARD_COMBINATIONS) {
            int sum3 = values[triple[0]] + values[triple[1]] + values[triple[2]];
            if (sum3 % 10 == 0) {
                // 计算剩下两张的和：总和 - 三张之和
                int totalSum = values[0] + values[1] + values[2] + values[3] + values[4];
                int sum2 = totalSum - sum3;
                int remainder = sum2 % 10;
                return remainder == 0 ? 10 : remainder;
            }
        }

        return 0;
    }
}
