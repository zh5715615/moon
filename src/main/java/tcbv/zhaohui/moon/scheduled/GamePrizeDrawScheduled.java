package tcbv.zhaohui.moon.scheduled;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.datatypes.Int;
import tcbv.zhaohui.moon.beans.CandleGraphBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbRewardRecordDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbRewardRecord;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.utils.BnbPriceUtil;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author dawn  游戏开奖
 * @date 2024/11/5 10:45
 */
//@Component
@Slf4j
public class GamePrizeDrawScheduled {
//    private final Integer gameTypeOne = 1;
//    private final Integer gameTypeTwo = 2;
//    private final Integer gameTypeThree = 3;
//    @Resource
//    private TbGameResultDao tbGameResultDao;
//    @Resource
//    private TbTxRecordDao tbTxRecordDao;
//    @Resource
//    private TbRewardRecordDao tbRewardRecordDao;
//
//    @Autowired
//    private Web3Config web3Config;
//
//    @Transactional
//    @Scheduled(fixedDelay = 20000)
//    public void executeTask() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime gameOneTime = TimerMaps.getRemainingTime(TimerMaps.GAMEONE);
//        LocalDateTime gameTwoTime = TimerMaps.getRemainingTime(TimerMaps.GAMETWO);
//        if (gameOneTime == null && gameTwoTime == null) {
//            log.error("开奖游戏剩余时间获取失败");
//            return;
//        }
//        // 获取当前时间
//        LocalDateTime now = LocalDateTime.now();
//        String oneSimpleTimes = gameOneTime.format(formatter);
//        String twoSimpleTimes = gameTwoTime.format(formatter);
//
//        // 计算两个时间之间的间隔
//        Duration oneTime = Duration.between(gameOneTime, now);
//        Duration twoTime = Duration.between(gameTwoTime, now);
//        // 获取间隔的绝对值，以秒为单位
//        long oneTimes = Math.abs(oneTime.getSeconds());
//        long twoTimes = Math.abs(twoTime.getSeconds());
//        Random rand = new Random();
//         Integer result = rand.nextInt(10);
//        if (oneTimes < 60) {
//            Integer turns = tbGameResultDao.findGameTypeNumber(gameTypeOne);
//            if (turns == null) {
//                turns = 0;
//            }
//            turns++;
//            clearingUserAward(gameTypeOne, turns, oneSimpleTimes, result);
//        }
//        if (twoTimes < 60) {
//            Integer turns = tbGameResultDao.findGameTypeNumber(gameTypeTwo);
//            if (turns == null) {
//                turns = 0;
//            }
//
//            turns++;
//            clearingUserAward(gameTypeTwo, turns, twoSimpleTimes, result);
//        }
//    }
//
//    //取摇色子的点数
//    private int singleAndDouble() {
//        //TODO 暂时用随机数代替，后续改为实际算法
//        return (int) (Math.random() * 16) + 3;
//    }
//
//    //BNB涨跌结果获取
//    private int raseAndFall() {
//        Pair<Long, Long> pair = CustomizeTimeUtil.getFiveMinuteRange(LocalDateTime.now(), 5);
//        long startTime = pair.getLeft();
//        long endTime = pair.getRight();
//        CandleGraphBean candleGraphBean = BnbPriceUtil.bnbUsdtKline(startTime, endTime,
//                web3Config.isProxy(), web3Config.getHostname(), web3Config.getPort());
//        return candleGraphBean.getClosePrice() > candleGraphBean.getOpenPrice() ? 1 : 2;
//    }
//
//    public void clearingUserAward(Integer gameType, Integer turns, String drawnTime, Integer result) {
//
//        //判断时间是否已经开奖
//        TbGameResult drawnTimeInfo = tbGameResultDao.findDrawnTimeInfo(drawnTime, gameType);
//        if (drawnTimeInfo != null) {
//            return;
//        }
//        List<TbRewardRecord> entities = Lists.newArrayList();
//        List<TbTxRecord> victory = Lists.newArrayList();
//        List<TbTxRecord> defeated = Lists.newArrayList();
//        //查询用户下注记录
//        List<TbTxRecord> turnsGameInfo = tbTxRecordDao.findTurnsGameInfo(turns, gameType);
//        if (gameType == gameTypeOne) {
//            //胜者
//            victory = turnsGameInfo.stream().filter(x -> x.getSingleAndDouble() == result).collect(Collectors.toList());
//            defeated = turnsGameInfo.stream().filter(x -> x.getSingleAndDouble() != result).collect(Collectors.toList());
//        } else if (gameType == gameTypeTwo) {
//            victory = turnsGameInfo.stream().filter(x -> x.getRaseAndFall() == result).collect(Collectors.toList());
//            defeated = turnsGameInfo.stream().filter(x -> x.getRaseAndFall() != result).collect(Collectors.toList());
//
//        }
//        //todo 开奖结果目前先随机数，后续接入
//        //开奖记录存储
//        entities.addAll(victory.stream().map(e -> {
//            return TbRewardRecord.builder()
//                    .id(UUID.randomUUID().toString()).turns(turns).userId(e.getUserId())
//                    .rewardAmount(e.getAmount().add(e.getAmount()))
//                    .gameType(gameType.toString()).createTime(drawnTime)
//                    //.txHash()
//                    .build();
//        }).collect(Collectors.toList()));
//        entities.addAll(defeated.stream().map(e -> {
//            return TbRewardRecord.builder()
//                    .id(UUID.randomUUID().toString()).turns(turns).userId(e.getUserId())
//                    .rewardAmount(e.getAmount().negate())
//                    .gameType(gameType.toString()).createTime(drawnTime)
//                    .txHash("1")
//                    .build();
//        }).collect(Collectors.toList()));
//        if(entities.size()>0){
////            tbRewardRecordDao.insertBatch(entities);
//        }
//        //最终结果录入
//        TbGameResult param = TbGameResult.builder()
//                .id(UUID.randomUUID().toString()).gameType(gameType)
//                .turns(turns).drawnTime(drawnTime)
//                .build();
//        if (gameType==gameTypeOne) {
//            param.setSingleAndDouble(result);
//        } else if (gameType==gameTypeTwo) {
//            param.setRaseAndFall(result);
//        }
//        tbGameResultDao.insert(param);
//    }

}
