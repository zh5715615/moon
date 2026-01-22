package tcbv.zhaohui.moon.controller;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import software.amazon.ion.Decimal;
import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.dto.BullfightingStartDto;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.entity.BullfightingPurchaseEntity;
import tcbv.zhaohui.moon.entity.BullfightingRecordEntity;
import tcbv.zhaohui.moon.entity.BullfightingRewardEntity;
import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import tcbv.zhaohui.moon.game.bullfighting.Card;
import tcbv.zhaohui.moon.game.bullfighting.PokerDealer;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.BullfightingPurchaseService;
import tcbv.zhaohui.moon.service.BullfightingRecordService;
import tcbv.zhaohui.moon.service.BullfightingRewardService;
import tcbv.zhaohui.moon.service.BullfightingScoreService;
import tcbv.zhaohui.moon.service.chain.BullfightingGameSampleService;
import tcbv.zhaohui.moon.utils.GsonUtil;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private BullfightingRecordService bullfightingRecordService;

    @Autowired
    private BullfightingScoreService bullfightingScoreService;

    @Autowired
    private BullfightingGameSampleService gameSampleService;

    @Autowired
    private BullfightingPurchaseService bullfightingPurchaseService;

    @Autowired
    private BullfightingRewardService bullfightingRewardService;

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

    private BullfightingScoreEntity saveBullfightingRecord(BullfightingStartVo bullfightingStartVo) {
        BullfightingRecordEntity bullfightingRecordEntity = new BullfightingRecordEntity();
        bullfightingRecordEntity.setScore(Math.abs(bullfightingStartVo.getRoundOfScore()));
        bullfightingRecordEntity.setUserId(JwtContext.getUserId());
        bullfightingRecordEntity.setWinlose(bullfightingStartVo.getSelf().getWin());
        bullfightingRecordEntity.setSelfCard(GsonUtil.toJson(bullfightingStartVo.getSelf(), true));
        bullfightingRecordEntity.setOtherCard(GsonUtil.toJson(bullfightingStartVo.getOther(), true));
        return bullfightingRecordService.save(bullfightingRecordEntity);
    }

    /**
     * 随机生成指定长度的16进制字符串
     * @param length 字符串长度
     * @return 16进制字符串
     */
    public static String generateHex(int length) {
        // Create a StringBuilder to store the generated hex string
        StringBuilder hexBuilder = new StringBuilder();
        // Define the characters used for generating the hex string
        String hexCharacters = "0123456789abcdef";
        // Loop until the desired length is reached
        for (int i = 0; i < length; i++) {
            // Generate a random index to select a character from hexCharacters
            int randomIndex = (int) (Math.random() * hexCharacters.length());
            // Get the character at the random index
            char randomChar = hexCharacters.charAt(randomIndex);
            // Append the random character to the hexBuilder
            hexBuilder.append(randomChar);
        }
        // Return the generated hex string
        return hexBuilder.toString();
    }

    @PostMapping("/start")
    @JwtAddressRequired
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
        String userAddress = JwtContext.getAddress();
        if (win > 0) {
            maxBullfightingUserCard.setAddress(userAddress);
            vo.setSelf(maxBullfightingUserCard);
            vo.setOther(other);
            for (BullfightingStartVo.UserCardVo item : other) {
                item.setAddress("0x" + generateHex(40));
            }
            vo.setRoundOfScore(dto.getScore());
        } else {
            BullfightingStartVo.UserCardVo selfVo = other.get(0);
            selfVo.setAddress(userAddress);
            vo.setSelf(selfVo);
            List<BullfightingStartVo.UserCardVo> other1 = new ArrayList<>(other.subList(1, other.size()));

            // 随机插入位置：0 到 other1.size()（包含）
            int randomIndex = ThreadLocalRandom.current().nextInt(other1.size() + 1);
            other1.add(randomIndex, maxBullfightingUserCard);
            for (BullfightingStartVo.UserCardVo item : other1) {
                item.setAddress("0x" + generateHex(40));
            }
            vo.setOther(other1);
            vo.setRoundOfScore(-dto.getScore());
        }

        BullfightingScoreEntity bullfightingScoreEntity = saveBullfightingRecord(vo);
        vo.setUserScores(bullfightingScoreEntity.getScore());
        vo.setRemainingTimes(bullfightingScoreEntity.getTimes());
        return Rsp.okData(vo);
    }

    @GetMapping("/user/statistics")
    @ApiOperation("用户斗牛统计信息")
    @JwtAddressRequired
    public Rsp<BullfightingUserStatisticsVo> userStatistics() {
        String userId = JwtContext.getUserId();
        BullfightingUserStatisticsVo vo = bullfightingScoreService.userStatistics(userId, new Date());
        return Rsp.okData(vo);
    }

    @GetMapping("/user/history")
    @ApiOperation("用户斗牛历史")
    @JwtAddressRequired
    public Rsp<RestPage<BullfightingHistoryVo>> userHistory(@RequestParam(value = "winlose", required = false) Boolean winlose, @RequestParam("pageIndex") int pageIndex, @RequestParam("pageSize") int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIndex <= 0 ? 0 : pageIndex - 1, pageSize, Sort.by(Sort.Direction.DESC, "create_time"));
        BullfightingRecordEntity bullfightingRecordEntity = new BullfightingRecordEntity();
        bullfightingRecordEntity.setUserId(JwtContext.getUserId());
        bullfightingRecordEntity.setWinlose(winlose);
        Page<BullfightingRecordEntity> pageEntity = this.bullfightingRecordService.queryByPage(bullfightingRecordEntity, pageRequest);
        if (pageEntity.getTotalElements() == 0) {
            return Rsp.okData(RestPage.empty());
        }
        List<BullfightingHistoryVo> bullfightingRecordVoList = new ArrayList<>();
        for (BullfightingRecordEntity resultEntity : pageEntity.getContent()) {
            BullfightingHistoryVo bullfightingRecordVo = new BullfightingHistoryVo();
            BeanUtils.copyProperties(resultEntity, bullfightingRecordVo);
            bullfightingRecordVo.setDate(resultEntity.getCreateTime());
            bullfightingRecordVoList.add(bullfightingRecordVo);
        }
        Page<BullfightingHistoryVo> pageVo = new PageImpl<>(bullfightingRecordVoList, pageRequest, pageEntity.getTotalElements());
        return Rsp.okData(RestPage.of(pageVo));
    }

    private List<BullfightingRankingItemVo> calcRank(String address, Date date) {
        PageRequest pageRequest = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "score"));
        BullfightingScoreEntity bullfightingScoreEntity = new BullfightingScoreEntity();
        bullfightingScoreEntity.setGameDate(date);
        Page<BullfightingScoreEntity> pageEntity = this.bullfightingScoreService.queryByPage(bullfightingScoreEntity, pageRequest);
        if (pageEntity.getTotalElements() == 0) {
            return Collections.emptyList();
        }
        List<BullfightingRankingItemVo> bullfightingRankingItemVoList = new ArrayList<>();
        for (BullfightingScoreEntity resultEntity : pageEntity.getContent()) {
            BullfightingRankingItemVo bullfightingScoreVo = new BullfightingRankingItemVo();
            bullfightingScoreVo.setAddress(resultEntity.getAddress());
            bullfightingScoreVo.setScore(resultEntity.getScore());
            if (address.equals(resultEntity.getAddress())) {
                bullfightingScoreVo.setSelf(true);
            }
            bullfightingRankingItemVoList.add(bullfightingScoreVo);
        }

        for (int i = 0; i < bullfightingRankingItemVoList.size(); i++) {
            BullfightingRankingItemVo current = bullfightingRankingItemVoList.get(i);

            if (i == 0) {
                current.setRank(1);
            } else {
                BullfightingRankingItemVo prev = bullfightingRankingItemVoList.get(i - 1);

                // 安全比较：假设 score 是 Integer
                boolean sameScore = Objects.equals(current.getScore(), prev.getScore());

                if (sameScore) {
                    current.setRank(prev.getRank());
                } else {
                    current.setRank(i + 1);
                }
            }
        }

        return bullfightingRankingItemVoList;
    }

    @GetMapping("/rankingList")
    @ApiOperation("斗牛排行榜")
    @JwtAddressRequired
    public Rsp<BullfightingRankingVo> rankingList() {
        String address = JwtContext.getAddress();
        String userId = JwtContext.getUserId();
        Date today = new Date();
        List<BullfightingRankingItemVo> bullfightingRankingItemVoList = calcRank(address, today);
        BullfightingScoreEntity userScoreEntity = this.bullfightingScoreService.userRanking(userId, today);
        BullfightingRankingVo bullfightingRankingVo = new BullfightingRankingVo();
        bullfightingRankingVo.setRankingList(bullfightingRankingItemVoList);
        if (userScoreEntity != null) {
            int score = userScoreEntity.getScore();
            bullfightingRankingVo.setRanking(userScoreEntity.getRank());
            bullfightingRankingVo.setScore(score);
            bullfightingRankingVo.setReward(Math.max(score, 0));
        }
        double todayPrizePool = gameSampleService.getPoolBalance().doubleValue();
        bullfightingRankingVo.setTodayPrizePool(todayPrizePool);
        return Rsp.okData(bullfightingRankingVo);
    }

    @PostMapping("/buyGameTimes")
    @ApiOperation("购买游戏次数")
    @JwtAddressRequired
    public Rsp buyGameTimes(@RequestBody @Valid TransactionDto dto) throws Exception {
        BuyGameTimesEventBean buyGameTimesEventBean = gameSampleService.parseBuyGameTimes(dto.getTxHash());
        String userId = JwtContext.getUserId();
        String address = JwtContext.getAddress();
        BullfightingPurchaseEntity bullfightingPurchaseEntity = new BullfightingPurchaseEntity();
        bullfightingPurchaseEntity.setAddress(address);
        bullfightingPurchaseEntity.setUserId(userId);
        bullfightingPurchaseEntity.setTimes(buyGameTimesEventBean.getTimes());
        bullfightingPurchaseEntity.setAmount(buyGameTimesEventBean.getAmount().doubleValue());
        bullfightingPurchaseEntity.setHash(dto.getTxHash());
        bullfightingPurchaseService.insert(bullfightingPurchaseEntity);
        return Rsp.ok();
    }

    private double queryYesterdayReward(String userId, String address, Date yesterday) {
        List<BullfightingRankingItemVo> rankingItemVoList = calcRank(address, yesterday);
        int totalScore = rankingItemVoList.stream()
                .mapToInt(BullfightingRankingItemVo::getScore)
                .filter(score -> score > 0)
                .sum();
        int rewardPool = bullfightingPurchaseService.queryRewardPoolByGameDate(yesterday);
        BullfightingScoreEntity userScoreEntity = this.bullfightingScoreService.userRanking(userId, yesterday);
        if (userScoreEntity == null) {
            return 0;
        }
        if (userScoreEntity.getRank() > 20) {
            return 0;
        }
        if (userScoreEntity.getScore() < 0) {
            return 0;
        }
        return (userScoreEntity.getScore() * 1.0 / totalScore) * (rewardPool * 0.95);
    }

    @GetMapping("/queryMyReward")
    @ApiOperation("查询我的奖励")
    @JwtAddressRequired
    public Rsp<Double> queryMyReward() {
        String userId = JwtContext.getUserId();
        String address = JwtContext.getAddress();
        Date yesterday = DateUtil.yesterday();
        double reward = queryYesterdayReward(userId, address, yesterday);
        BigDecimal bd = BigDecimal.valueOf(reward);
        BigDecimal floorReward = bd.setScale(0, RoundingMode.FLOOR);
        return Rsp.okData(floorReward.doubleValue());
    }

//    @PutMapping("/claimReward")
//    @ApiOperation("领取奖励")
//    @JwtAddressRequired
//    public Rsp claimReward() throws Exception {
//        String address = JwtContext.getAddress();
//        String userId = JwtContext.getUserId();
//        Date yesterday = DateUtil.yesterday();
//
//        double reward = queryYesterdayReward(userId, address, yesterday);
//        BigDecimal balance = gameSampleService.getPoolBalance();
//        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
//            return Rsp.error("奖池余额不足");
//        }
//        if (reward > 0) {
//            BigDecimal bd = BigDecimal.valueOf(reward);
//            String txHash = gameSampleService.reward(address, bd.setScale(0, RoundingMode.FLOOR));
//            BullfightingRewardEntity rewardEntity = new BullfightingRewardEntity();
//            rewardEntity.setAddress(address);
//            rewardEntity.setHash(txHash);
//            rewardEntity.setUserId(JwtContext.getUserId());
//            rewardEntity.setGameDate(yesterday);
//            rewardEntity.setAmount((double) reward);
//            bullfightingRewardService.insert(rewardEntity);
//            return Rsp.ok();
//        }
//        return Rsp.error("没有可领取的奖励");
//    }
}
