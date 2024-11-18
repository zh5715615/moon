package tcbv.zhaohui.moon.scheduled;

import com.google.common.collect.TreeMultimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.service.IUSDTLikeInterfaceService;
import tcbv.zhaohui.moon.service.impl.MoonNFTService;

import java.math.BigDecimal;
import java.util.*;

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

    public Map<String, Integer> owners() {
        long total = 0;
        try {
            total = moonNFTService.totalSupply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> ownersMap = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            String userAddress;
            try {
                userAddress = moonNFTService.ownerOf(i);
            } catch (Exception e) {
                continue;
            }
            if (ownersMap.containsKey(userAddress)) {
                ownersMap.put(userAddress, ownersMap.get(userAddress) + 1);
            } else {
                ownersMap.put(userAddress, 1);
            }
        }
        return ownersMap;
    }

    public static TreeMultimap<Integer, String> reverseAndSortUsingTreeMultimap(Map<String, Integer> inputMap) {
        // 创建一个新的 TreeMultimap，并指定键的排序方式为从大到小
        TreeMultimap<Integer, String> multimap = TreeMultimap.create(Collections.reverseOrder(), Comparator.naturalOrder());

        // 遍历输入的 Map，将键值对反转并存入 TreeMultimap 中
        for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
            multimap.put(entry.getValue(), entry.getKey());
        }

        return multimap;
    }

    @Scheduled(cron = "0 0 21 ? * SUN")
    public void executeWeeklyTask() throws Exception {
        log.info("执行周奖励任务");
        BigDecimal balance = usdtLikeInterfaceService.queryErc20Balance(web3Config.getWeekPoolAddress());
        long total = moonNFTService.totalSupply();
        Map<String, Integer> ownersMap = owners();
        ownersMap.forEach((userAddress, count) -> {
            try {
                double percent = (count * 100000000.0 / total) / 100000000;
                BigDecimal reward = balance.multiply(BigDecimal.valueOf(percent));
                usdtLikeInterfaceService.transferWeek(userAddress, reward);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Scheduled(cron = "0 0 21 L * ?")
    public void executeMonthlyTask() throws Exception {
        log.info("执行月奖励任务");
        BigDecimal balance = usdtLikeInterfaceService.queryErc20Balance(web3Config.getMonthPoolAddress());
        BigDecimal no1 = balance.multiply(BigDecimal.valueOf(0.125));
        BigDecimal no2 = balance.multiply(BigDecimal.valueOf(0.075));
        BigDecimal no3 = balance.multiply(BigDecimal.valueOf(0.05));
        BigDecimal no4_10 = balance.multiply(BigDecimal.valueOf(0.2)).divide(BigDecimal.valueOf(7.0));
        BigDecimal no11_25 = balance.multiply(BigDecimal.valueOf(0.25)).divide(BigDecimal.valueOf(15.0));
        BigDecimal no26_50 = balance.multiply(BigDecimal.valueOf(0.3)).divide(BigDecimal.valueOf(25.0));

        TreeMultimap<Integer, String> ownersMap = reverseAndSortUsingTreeMultimap(owners());
        int i = 0;
        for (Map.Entry<Integer, String> entry : ownersMap.entries()) {
            String userAddress = entry.getValue();
            BigDecimal reward = BigDecimal.ZERO;
            if (i == 0) {
                reward = no1;
            }
            if (i == 2) {
                reward = no2;
            }
            if (i == 3) {
                reward = no3;
            }
            if (i >= 4 && i <= 10) {
                reward = no4_10;
            }
            if (i >= 11 && i <= 25) {
                reward = no11_25;
            }
            if (i >= 26 && i <= 50) {
                reward = no26_50;
            }
            i++;
            if (reward.compareTo(BigDecimal.ZERO) > 0) {
                usdtLikeInterfaceService.transferMooth(userAddress, reward);
            }
        }
    }
}
