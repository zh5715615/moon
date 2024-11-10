package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
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

@Component
@Slf4j
public class DiceRollerSchedule extends AllocReward {

    @Resource
    private TbTxRecordDao txRecordDao;

    @Resource
    private TbGameResultDao gameResultDao;

    @Resource
    private TbUserDao userDao;

    @Autowired
    private IMoonBaseService moonBaseService;

    @Scheduled(cron = "0 0/10 * * * ? ")
    public void startScheduleOn() {
        Integer gameTurns = gameResultDao.maxTurns(1);
        if (gameTurns == null) {
            gameTurns = 0;
        }
        gameTurns++;
        log.info("第{}轮摇骰子投注时间，可以下注.", gameTurns);
        TbGameResult gameResult = new TbGameResult();
        gameResult.setId(UUID.randomUUID().toString());
        gameResult.setGameType(1);
        gameResult.setTurns(gameTurns);
        gameResultDao.insert(gameResult);
    }

    @Scheduled(cron = "0 9/10 * * * ? ")
    public void startScheduleOff() {
        log.info("摇骰子开奖时间，不能下注.");
        Integer gameTurns = gameResultDao.maxTurns(1);
        if (gameTurns == null) {
            return;
        }
        TbGameResult gameResult = gameResultDao.findGameTypeAndTurnsInfo(gameTurns, 1);
        if (gameResult == null) {
            return;
        }
        List<TbTxRecord> txRecordList = txRecordDao.findTurnsGameInfo(gameTurns,1);
        List<String> txHashList = txRecordList.stream().map(TbTxRecord::getTxHash).collect(Collectors.toList());
        int result = 0;
        if (!CollectionUtils.isEmpty(txHashList)) {
            BigInteger sum = BigInteger.ZERO;
            for (String s : txHashList) {
                BigInteger b = new BigInteger(s, 16);
                sum = sum.and(b);
            }
            result = sum.mod(BigInteger.valueOf(16)).add(BigInteger.valueOf(3)).intValue();
        }
        gameResult.setSingleAndDouble(result);
        gameResult.setDrawnTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
        gameResultDao.update(gameResult);

        allocReward(moonBaseService, userDao, txRecordDao, gameTurns, result % 2 + 1);
    }
}
