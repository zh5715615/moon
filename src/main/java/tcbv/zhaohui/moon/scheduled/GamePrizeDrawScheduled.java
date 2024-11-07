package tcbv.zhaohui.moon.scheduled;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.datatypes.Int;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbRewardRecordDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbRewardRecord;
import tcbv.zhaohui.moon.entity.TbTxRecord;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author dawn  游戏开奖
 * @date 2024/11/5 10:45
 */
@Component
@Slf4j
public class GamePrizeDrawScheduled {
    private final Integer gameTypeOne = 1;
    private final Integer gameTypeTwo = 2;
    private final Integer gameTypeThree = 3;
    @Resource
    private TbGameResultDao tbGameResultDao;
    @Resource
    private TbTxRecordDao tbTxRecordDao;
    @Resource
    private TbRewardRecordDao tbRewardRecordDao;

    @Scheduled(fixedDelay = 20000)
    public void executeTask() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime gameOneTime = TimerMaps.getRemainingTime(TimerMaps.GAMEONE);
        LocalDateTime gameTwoTime = TimerMaps.getRemainingTime(TimerMaps.GAMETWO);
        if (gameOneTime == null && gameTwoTime == null) {
            log.error("开奖游戏剩余时间获取失败");
            return;
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String oneSimpleTimes = gameOneTime.format(formatter);
        String twoSimpleTimes = gameTwoTime.format(formatter);

        // 计算两个时间之间的间隔
        Duration oneTime = Duration.between(gameOneTime, now);
        Duration twoTime = Duration.between(gameTwoTime, now);
        // 获取间隔的绝对值，以秒为单位
        long oneTimes = Math.abs(oneTime.getSeconds());
        long twoTimes = Math.abs(twoTime.getSeconds());
        if (oneTimes < 60) {
            Integer turns = tbGameResultDao.findGameTypeNumber(gameTypeOne);
            if (turns == null) {
                turns = 0;
            }
            turns++;
            clearingUserAward(gameTypeOne, turns, oneSimpleTimes, null);
        }
        if (twoTimes < 60) {
            Integer turns = tbGameResultDao.findGameTypeNumber(gameTypeTwo);
            if (turns == null) {
                turns = 0;
            }
            turns++;
            clearingUserAward(gameTypeTwo, turns, twoSimpleTimes, null);
        }
    }

    public void clearingUserAward(Integer gameType, Integer turns, String drawnTime, Integer result) {
        //判断时间是否已经开奖
        TbGameResult drawnTimeInfo = tbGameResultDao.findDrawnTimeInfo(drawnTime, gameType);
        if (drawnTimeInfo != null) {
            return;
        }
        List<TbRewardRecord> entities = Lists.newArrayList();
        List<TbTxRecord> victory = Lists.newArrayList();
        List<TbTxRecord> defeated = Lists.newArrayList();
        //查询用户下注记录
        List<TbTxRecord> turnsGameInfo = tbTxRecordDao.findTurnsGameInfo(turns, gameType);
        //todo 需要确认一下看是批量还是单个上链
        if (gameType == gameTypeOne) {
            //胜者
            victory = turnsGameInfo.stream().filter(x -> x.getSingleAndDouble() == result).collect(Collectors.toList());
            defeated = turnsGameInfo.stream().filter(x -> x.getSingleAndDouble() != result).collect(Collectors.toList());
        } else if (gameType == gameTypeTwo) {
            victory = turnsGameInfo.stream().filter(x -> x.getRaseAndFall() == result).collect(Collectors.toList());
            defeated = turnsGameInfo.stream().filter(x -> x.getRaseAndFall() != result).collect(Collectors.toList());

        }
        //开奖记录存储
        entities.addAll(victory.stream().map(e -> {
            return TbRewardRecord.builder()
                    .id(UUID.randomUUID().toString()).turns(turns).userId(e.getUserId())
                    .rewardAmount(new BigDecimal(e.getAmount()).add(new BigDecimal(e.getAmount())))
                    .gameType(gameType.toString()).createTime(drawnTime)
                    //.txHash()
                    .build();
        }).collect(Collectors.toList()));
        entities.addAll(defeated.stream().map(e -> {
            return TbRewardRecord.builder()
                    .id(UUID.randomUUID().toString()).turns(turns).userId(e.getUserId())
                    .rewardAmount(new BigDecimal(e.getAmount()).negate())
                    .gameType(gameType.toString()).createTime(drawnTime)
                    .txHash("1")
                    .build();
        }).collect(Collectors.toList()));
        if(entities.size()>0){
            tbRewardRecordDao.insertBatch(entities);
            //最终结果录入
            TbGameResult param = TbGameResult.builder()
                    .id(UUID.randomUUID().toString()).gameType(gameType)
                    .turns(turns).drawnTime(drawnTime)
                    .build();
            if (gameType==gameTypeOne) {
                param.setSingleAndDouble(result);
            } else if (gameType==gameTypeTwo) {
                param.setRaseAndFall(result);
            }
            tbGameResultDao.insert(param);
        }

    }

}
