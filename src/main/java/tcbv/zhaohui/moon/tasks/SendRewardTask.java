package tcbv.zhaohui.moon.tasks;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.beans.PresaleInfoBean;
import tcbv.zhaohui.moon.entity.BullfightingRewardEntity;
import tcbv.zhaohui.moon.entity.BullfightingScoreEntity;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.BullfightingPurchaseService;
import tcbv.zhaohui.moon.service.BullfightingRewardService;
import tcbv.zhaohui.moon.service.BullfightingScoreService;
import tcbv.zhaohui.moon.service.chain.BullfightingGameSampleService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.vo.BullfightingRankingItemVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static tcbv.zhaohui.moon.beans.Constants.PRESALE_TOTAL;
import static tcbv.zhaohui.moon.exceptions.ChainException.INVOKE_EXCEPTION;

/**
 * @author: zhaohui
 * @Title: CreateLiquidityPoolTask
 * @Description:
 * @date: 2026/1/15 21:44
 */
@Component
@Slf4j
public class SendRewardTask {

    @Autowired
    private BullfightingGameSampleService gameSampleService;

    @Autowired
    private BullfightingScoreService bullfightingScoreService;

    @Autowired
    private BullfightingPurchaseService bullfightingPurchaseService;

    @Autowired
    private BullfightingRewardService bullfightingRewardService;

    private List<BullfightingRankingItemVo> calcRank(Date date) {
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
            bullfightingScoreVo.setUserId(resultEntity.getUserId());
            bullfightingScoreVo.setAddress(resultEntity.getAddress());
            bullfightingScoreVo.setScore(resultEntity.getScore());
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

    private double queryYesterdayReward(String userId, Date yesterday) {
        List<BullfightingRankingItemVo> rankingItemVoList = calcRank(yesterday);
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

    @Scheduled(cron = "0 0 2 * * ?")
    public void hourlyTask() {
        log.info("当前时间：{}，执行每十分钟任务。", new Date());
        Date yesterday = DateUtil.yesterday();
        List<BullfightingRankingItemVo> rankingItemVoList = calcRank(yesterday);
        if (CollectionUtils.isEmpty(rankingItemVoList)) {
            log.warn("没有排行榜数据，不执行奖励发放。");
            return;
        }
        List<BullfightingRewardEntity> bullfightingRewardEntityList = new ArrayList<>();
        List<String> addressList = new ArrayList<>();
        List<BigDecimal> amountList = new ArrayList<>();
        Date date = new Date();
        double rewardSum = 0;
        for (BullfightingRankingItemVo rankingItemVo : rankingItemVoList) {
            if (rankingItemVo.getScore() > 0) {
                double reward = queryYesterdayReward(rankingItemVo.getUserId(), yesterday);
                BigDecimal bd = BigDecimal.valueOf(reward);
                BigDecimal floorReward = bd.setScale(0, RoundingMode.FLOOR);
                if (reward > 0) {
                    addressList.add(rankingItemVo.getAddress());
                    amountList.add(floorReward);
                    BullfightingRewardEntity rewardEntity = new BullfightingRewardEntity();
                    rewardEntity.setId(UUID.randomUUID().toString());
                    rewardEntity.setAddress(rankingItemVo.getAddress());
                    rewardEntity.setUserId(rankingItemVo.getUserId());
                    rewardEntity.setGameDate(yesterday);
                    rewardEntity.setAmount(floorReward.doubleValue());
                    rewardEntity.setCreateTime(date);
                    bullfightingRewardEntityList.add(rewardEntity);
                    rewardSum += floorReward.doubleValue();
                }
            }
        }
        try {
            boolean exist = bullfightingRewardService.existGamedate(yesterday);
            if (exist) {
                log.error("已经发放过奖励");
                return;
            }
            BigDecimal balance = gameSampleService.getPoolBalance();
            if (balance.compareTo(BigDecimal.valueOf(rewardSum)) <= 0) {
                log.error("奖池余额不足");
                return;
            }
            String txHash = gameSampleService.reward(addressList, amountList);
            bullfightingRewardEntityList.forEach(entity -> entity.setHash(txHash));
            bullfightingRewardService.insertBatch(bullfightingRewardEntityList);
        } catch (Exception e) {
            log.error("奖励发放失败", e);
        }
    }
}
