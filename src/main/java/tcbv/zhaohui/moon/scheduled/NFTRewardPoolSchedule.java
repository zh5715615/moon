package tcbv.zhaohui.moon.scheduled;

import com.google.common.collect.TreeMultimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.entity.TbNftReward;
import tcbv.zhaohui.moon.service.IUSDTLikeInterfaceService;
import tcbv.zhaohui.moon.service.TbNftRewardService;
import tcbv.zhaohui.moon.service.impl.MoonNFTService;
import tcbv.zhaohui.moon.vo.NFTRankVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@ConditionalOnProperty(name = "moon.scheduled", havingValue = "true")
@Slf4j
public class NFTRewardPoolSchedule {

    @Autowired
    private MoonNFTService moonNFTService;

    @Autowired
    private IUSDTLikeInterfaceService usdtLikeInterfaceService;

    @Autowired
    private Web3Config web3Config;

    @Autowired
    private TbNftRewardService nftRewardService;

    @Scheduled(cron = "0 0 21 ? * SUN")
    public void executeWeeklyTask() throws Exception {
        log.info("执行周奖励任务");
        BigDecimal balance = usdtLikeInterfaceService.queryErc20Balance(web3Config.getWeekPoolAddress());
        long total = moonNFTService.totalSupply();
        Map<String, Integer> ownersMap = moonNFTService.owners();

        List<TbNftReward> nftRewardList = new ArrayList<>();
        nftRewardService.deleteByCycle(MoonConstant.NFT_WEEK_CYCLE);

        AtomicInteger i = new AtomicInteger();
        ownersMap.forEach((userAddress, count) -> {
            try {
                double percent = (count * 100000000.0 / total) / 100000000;
                BigDecimal reward = balance.multiply(BigDecimal.valueOf(percent));
                String txHash = usdtLikeInterfaceService.transferWeek(userAddress, reward);
                TbNftReward nftReward = new TbNftReward();
                nftReward.setUserAddress(userAddress);
                nftReward.setNftAmount(count);
                nftReward.setRewardAmount(reward.doubleValue());
                nftReward.setCycle(MoonConstant.NFT_WEEK_CYCLE);
                nftReward.setTxHash(txHash);
                nftReward.setCreateTime(new Date());
                if (i.get() < MoonConstant.NFT_WEEK_COUNT) {
                    nftRewardList.add(nftReward);
                }
                i.getAndIncrement();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        nftRewardService.insertBatch(nftRewardList);
    }

    @Scheduled(cron = "0 0 21 28-31 * ?")
    public void executeMonthlyTask() throws Exception {
        log.info("执行月奖励任务");
        LocalDateTime localDateTime = LocalDateTime.now();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            if (day != 31) {
                return;
            }
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            if (day != 30) {
                return;
            }
        }
        int year = localDateTime.getYear();
        boolean isLeapYear = Year.isLeap(year);
        if (isLeapYear) {
            if (day != 29) {
                return;
            }
        } else {
            if (day != 28) {
                return;
            }
        }

        BigDecimal balance = usdtLikeInterfaceService.queryErc20Balance(web3Config.getMonthPoolAddress());
        BigDecimal no1 = balance.multiply(BigDecimal.valueOf(0.125));
        BigDecimal no2 = balance.multiply(BigDecimal.valueOf(0.075));
        BigDecimal no3 = balance.multiply(BigDecimal.valueOf(0.05));
        BigDecimal no4_10 = balance.multiply(BigDecimal.valueOf(0.2)).divide(BigDecimal.valueOf(7.0));
        BigDecimal no11_25 = balance.multiply(BigDecimal.valueOf(0.25)).divide(BigDecimal.valueOf(15.0));
        BigDecimal no26_50 = balance.multiply(BigDecimal.valueOf(0.3)).divide(BigDecimal.valueOf(25.0));

        TreeMultimap<Integer, String> ownersMap = moonNFTService.nftRank();
        int i = 1;
        List<TbNftReward> nftRewardList = new ArrayList<>();
        nftRewardService.deleteByCycle(MoonConstant.NFT_MONTH_CYCLE);
        for (Map.Entry<Integer, String> entry : ownersMap.entries()) {
            int count = entry.getKey();
            String userAddress = entry.getValue();
            BigDecimal reward = BigDecimal.ZERO;
            if (i == 1) {
                reward = no1;
            } else if (i == 2) {
                reward = no2;
            } else if (i == 3) {
                reward = no3;
            } else if (i >= 4 && i <= 10) {
                reward = no4_10;
            } else if (i >= 11 && i <= 25) {
                reward = no11_25;
            } else if (i >= 26 && i <= 50) {
                reward = no26_50;
            } else {
                break;
            }
            i++;
            if (reward.compareTo(BigDecimal.ZERO) > 0) {
                String txHash = usdtLikeInterfaceService.transferMooth(userAddress, reward);
                TbNftReward nftReward = new TbNftReward();
                nftReward.setUserAddress(userAddress);
                nftReward.setNftAmount(count);
                nftReward.setRewardAmount(reward.doubleValue());
                nftReward.setCycle(MoonConstant.NFT_MONTH_CYCLE);
                nftReward.setTxHash(txHash);
                nftReward.setCreateTime(new Date());
                nftRewardList.add(nftReward);
            }
        }
        nftRewardService.insertBatch(nftRewardList);
    }
    @Scheduled(cron = "0 0 * * * ?")
    public void realtimeRank() {
        moonNFTService.realtimeRank();
    }

}
