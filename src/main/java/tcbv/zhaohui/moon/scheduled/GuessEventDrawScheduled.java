package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.*;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.service.IEventTaskManager;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class GuessEventDrawScheduled extends AllocReward implements Runnable {

    private String eventName;

    private String eventId;

    private String optionId;

    private TbGameResultDao gameResultDao;

    private TbTxRecordDao txRecordDao;

    private IEventTaskManager eventTaskManager;

    private IMoonBaseService moonBaseService;

    private TbUserDao userDao;

    private TbRewardRecordDao rewardRecordDao;

    private TbEventDao eventDao;

    public GuessEventDrawScheduled(IMoonBaseService moonBaseService, TbUserDao userDao, TbTxRecordDao txRecordDao, TbRewardRecordDao rewardRecordDao, TbEventDao eventDao,
                                   IEventTaskManager eventTaskManager, TbGameResultDao gameResultDao, String eventName, String eventId, String optionId) {
        this.moonBaseService = moonBaseService;
        this.userDao = userDao;
        this.txRecordDao = txRecordDao;
        this.rewardRecordDao = rewardRecordDao;
        this.eventDao = eventDao;
        this.eventTaskManager = eventTaskManager;
        this.gameResultDao = gameResultDao;
        this.eventName = eventName;
        this.eventId = eventId;
        this.optionId = optionId;
    }

    @Override
    public void run() {
        log.info("摇骰子开奖时间，不能下注.");
        Integer gameTurns = gameResultDao.maxTurns(getGameType());
        if (gameTurns == null) {
            return;
        }
        TbGameResult gameResult = gameResultDao.findGameTypeAndTurnsInfo(gameTurns, getGameType());
        if (gameResult == null) {
            return;
        }
        gameResult.setEventResult(optionId);
        gameResult.setDrawnTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
        gameResultDao.update(gameResult);

        allocReward(moonBaseService, userDao, txRecordDao, rewardRecordDao, gameTurns, optionId);

        TbEvent event = new TbEvent();
        event.setId(eventId);
        event.setStatus(MoonConstant.EVENT_DRAW_LOTTO);
        eventDao.update(event);
    }

    private void allocReward(IMoonBaseService moonBaseService, TbUserDao userDao, TbTxRecordDao txRecordDao,
                             TbRewardRecordDao rewardRecordDao, int turns, String optionId) {
        double loserAmount = txRecordDao.betEventErrorNumber(getGameType(), turns, optionId);
        double winnerAmount = txRecordDao.betEventCorrectNumber(getGameType(), turns, optionId);
        double poolTotalAmount = loserAmount + winnerAmount;
        List<TbTxRecord> winnerList = txRecordDao.winnerEventList(getGameType(), turns, optionId);
        List<TbTxRecord> loserList = txRecordDao.loserEventList(getGameType(), turns, optionId);
        saveRewardRecord(winnerList, loserList, loserAmount, poolTotalAmount, userDao, rewardRecordDao, moonBaseService);
    }

    @Override
    protected Integer getGameType() {
        return MoonConstant.GUESS_EVENT_GAME;
    }
}
