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

    private static final int[][] THREE_CARD_COMBINATIONS = {
            {0, 1, 2}, {0, 1, 3}, {0, 1, 4},
            {0, 2, 3}, {0, 2, 4},
            {0, 3, 4},
            {1, 2, 3}, {1, 2, 4},
            {1, 3, 4},
            {2, 3, 4}
    };

    // 常量：牛牛游戏的标准配置
    private static final int BULLFIGHTING_PLAYER_COUNT = 5;
    private static final int BULLFIGHTING_CARDS_PER_HAND = 5;
    private static final int MAX_BULLFIGHTING_RETRIES = 100;

    // 成员 Random，避免频繁创建
    private final Random random = new Random();

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
     * 洗牌并发牌。
     * - 若为 5 人 × 5 张（牛牛标准局），则自动启用合规校验：
     *     1. 至少有一手有牛（score > 0）
     *     2. 所有有牛手中，最大牛值唯一（无并列冠军）
     * - 其他情况：直接发牌，不校验。
     *
     * @param handCount      玩家数量
     * @param cardsPerHand   每人发牌张数
     * @return 发好的手牌列表
     */
    public List<List<Card>> dealHands(int handCount, int cardsPerHand) {
        // 非牛牛标准局：直接发牌
        if (handCount != BULLFIGHTING_PLAYER_COUNT || cardsPerHand != BULLFIGHTING_CARDS_PER_HAND) {
            List<Card> deck = createDeck();
            Collections.shuffle(deck, random);

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

        // === 牛牛标准局：启用合规校验 ===
        int attempts = 0;
        while (attempts++ < MAX_BULLFIGHTING_RETRIES) {
            List<Card> deck = createDeck();
            Collections.shuffle(deck, random);

            List<List<Card>> hands = new ArrayList<>();
            int index = 0;
            for (int i = 0; i < BULLFIGHTING_PLAYER_COUNT; i++) {
                List<Card> hand = new ArrayList<>();
                for (int j = 0; j < BULLFIGHTING_CARDS_PER_HAND; j++) {
                    hand.add(deck.get(index++));
                }
                hands.add(hand);
            }

            // 计算每手的牛值
            List<Integer> scores = new ArrayList<>();
            for (List<Card> hand : hands) {
                scores.add(calculateHandValue(hand));
            }

            // 过滤出所有有牛的手牌（score > 0）
            List<Integer> validScores = scores.stream()
                    .filter(score -> score > 0)
                    .toList();

            // 条件1：至少有一个有牛
            if (validScores.isEmpty()) {
                continue;
            }

            // 条件2：最大牛值必须唯一
            int maxScore = Collections.max(validScores);
            long countOfMax = validScores.stream().filter(s -> s == maxScore).count();

            if (countOfMax == 1) {
                return hands; // ✅ 合规牌局，返回
            }
            // 否则：存在并列最高牛值，重试
        }

        throw new IllegalStateException(
                "无法在 " + MAX_BULLFIGHTING_RETRIES + " 次内生成合规的牛牛牌局（要求：至少一个有牛，且最大牛值唯一）"
        );
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
                int totalSum = values[0] + values[1] + values[2] + values[3] + values[4];
                int sum2 = totalSum - sum3;
                int remainder = sum2 % 10;
                return remainder == 0 ? 10 : remainder;
            }
        }

        return 0;
    }
}
