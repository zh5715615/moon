package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.BullfightingStartDto;
import tcbv.zhaohui.moon.game.bullfighting.Card;
import tcbv.zhaohui.moon.game.bullfighting.PokerDealer;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.BullfightingStartVo;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: zhaohui
 * @Title: BullfightingController
 * @Description:
 * @date: 2026/1/12 21:44
 */
@RestController
@RequestMapping("/api/v1/moon/bullfighting")
@Validated
@ResponseBody
@Api(tags="斗牛游戏")
@Slf4j
public class BullfightingController {

    @Autowired
    private PokerDealer pokerDealer;

    private static final Random random = new Random();

    /**
     * 根据押注积分尝试赢牌
     *
     * @param paymentAmount 支付金额（1～5）
     * @return 奖励积分（0）
     */
    public int tryExtendCard(int paymentAmount) {
        // 校验输入
        if (paymentAmount < 1 || paymentAmount > 5) {
            throw new IllegalArgumentException("支付金额必须是 1～5");
        }

        // 定义概率和效果（按支付金额索引）
        double[] successRates = {0.8, 0.6, 0.4, 0.2, 0.1}; // 对应 1～5 亿
        int[] cardExtensions = {1, 2, 3, 4, 5};

        double successRate = successRates[paymentAmount - 1];
        int extensionScore = cardExtensions[paymentAmount - 1];

        // 生成 [0.0, 1.0) 的随机数
        double rand = random.nextDouble();

        // 判断是否成功
        if (rand < successRate) {
            System.out.println("🎉 恭喜！支付 " + paymentAmount + "积分成功，获得" + extensionScore + "积分！");
            return extensionScore;
        } else {
            System.out.println("💔 抱歉！支付 " + paymentAmount + "积分失败，输掉" + (-extensionScore) + "积分！");
            return -extensionScore;
        }
    }

    @PostMapping("/start")
    public Rsp<BullfightingStartVo> start(@RequestBody @Valid BullfightingStartDto dto) {
        int win = tryExtendCard(dto.getScore());

        List<List<Card>> hands = pokerDealer.dealHands(5, 5);
        BullfightingStartVo vo = new BullfightingStartVo();
        List<BullfightingStartVo.UserCardVo> userCardVoList = new ArrayList<>();
        for (List<Card> hand : hands) {
            BullfightingStartVo.UserCardVo userCardVo = new BullfightingStartVo.UserCardVo();
            userCardVo.setBullfighting(pokerDealer.calculateHandValue(hand));
            userCardVo.setPlayingCards(hand.stream().map(Card::toString).reduce((a, b) -> a + "," + b).orElse(""));
            userCardVoList.add(userCardVo);
        }
        Optional<BullfightingStartVo.UserCardVo> maxBullfightingUser = userCardVoList.stream()
                .max(Comparator.comparingInt(BullfightingStartVo.UserCardVo::getBullfighting));
        maxBullfightingUser.ifPresent(userCardVo -> userCardVo.setWin(Boolean.TRUE));
        BullfightingStartVo.UserCardVo maxBullfightingUserCard = maxBullfightingUser.orElse(null);
        if (maxBullfightingUserCard == null) {
            return Rsp.error("没有出现赢家");
        }
        List<BullfightingStartVo.UserCardVo> other = userCardVoList.stream().filter(userCardVo -> !userCardVo.equals(maxBullfightingUserCard)).toList();
        if (win > 0) {
            vo.setSelf(maxBullfightingUserCard);
            vo.setOther(other);
            vo.setRoundOfScore(dto.getScore());
        } else {
            vo.setSelf(other.get(0));
            List<BullfightingStartVo.UserCardVo> other1 = new ArrayList<>(other.subList(1, other.size()));

            // 随机插入位置：0 到 other1.size()（包含）
            int randomIndex = ThreadLocalRandom.current().nextInt(other1.size() + 1);
            other1.add(randomIndex, maxBullfightingUserCard);
            vo.setOther(other1);
            vo.setRoundOfScore(-dto.getScore());
        }

        return Rsp.okData(vo);
    }
}
