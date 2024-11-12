package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbRewardRecordDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

//@Component
@Slf4j
public class DiceRollerSchedule extends AllocReward {

    @Resource
    private TbTxRecordDao txRecordDao;

    @Resource
    private TbGameResultDao gameResultDao;

    @Resource
    private TbRewardRecordDao rewardRecordDao;

    @Resource
    private TbUserDao userDao;

    @Autowired
    private IMoonBaseService moonBaseService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void startScheduleOn() {
        TimerMaps.startDiceRoller();
        Integer gameTurns = gameResultDao.maxTurns(getGameType());
        if (gameTurns == null) {
            gameTurns = 0;
        }
        gameTurns++;
        log.info("第{}轮摇骰子投注时间，可以下注.", gameTurns);
        TbGameResult gameResult = new TbGameResult();
        gameResult.setId(UUID.randomUUID().toString());
        gameResult.setGameType(getGameType());
        gameResult.setTurns(gameTurns);
        gameResultDao.insert(gameResult);
    }

    @Scheduled(cron = "0 9/10 * * * ? ")
    public void startScheduleOff() {
        TimerMaps.stopDiceRoller();
        log.info("摇骰子开奖时间，不能下注.");
        Integer gameTurns = gameResultDao.maxTurns(getGameType());
        if (gameTurns == null) {
            return;
        }
        TbGameResult gameResult = gameResultDao.findGameTypeAndTurnsInfo(gameTurns, getGameType());
        if (gameResult == null) {
            return;
        }
        List<TbTxRecord> txRecordList = txRecordDao.findTurnsGameInfo(gameTurns,getGameType());
        List<String> txHashList = txRecordList.stream().map(TbTxRecord::getTxHash).collect(Collectors.toList());
        int result = 0;
        if (!CollectionUtils.isEmpty(txHashList)) {
            BigInteger sum = BigInteger.ZERO;
            for (String s : txHashList) {
                BigInteger b = new BigInteger(s.substring(2), 16);
                sum = sum.and(b);
            }
            result = sum.mod(BigInteger.valueOf(16)).add(BigInteger.valueOf(3)).intValue();
        }
        gameResult.setSingleAndDouble(result);
        gameResult.setDrawnTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
        gameResultDao.update(gameResult);

        allocReward(moonBaseService, userDao, txRecordDao, rewardRecordDao, gameTurns, 2 - result % 2);
    }

    @Override
    protected Integer getGameType() {
        return MoonConstant.DICE_ROLLER_GAME;
    }
}
