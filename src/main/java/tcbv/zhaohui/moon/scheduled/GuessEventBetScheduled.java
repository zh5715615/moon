package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.service.IEventTaskManager;

import java.util.UUID;

@Slf4j
public class GuessEventBetScheduled extends AllocReward implements Runnable {

    private String eventName;

    private String eventId;

    private TbGameResultDao gameResultDao;

    private IEventTaskManager eventTaskManager;

    public GuessEventBetScheduled(IEventTaskManager eventTaskManager, TbGameResultDao gameResultDao, String eventName, String eventId) {
        this.eventTaskManager = eventTaskManager;
        this.gameResultDao = gameResultDao;
        this.eventName = eventName;
        this.eventId = eventId;
    }

    @Override
    public void run() {
        TimerMaps.startGuessEvent();
        Integer gameTurns = gameResultDao.maxTurns(getGameType());
        if (gameTurns == null) {
            gameTurns = 0;
        }
        gameTurns++;
        log.info("第{}轮猜事件投注时间，可以下注.", gameTurns);
        TbGameResult gameResult = new TbGameResult();
        gameResult.setId(UUID.randomUUID().toString());
        gameResult.setGameType(getGameType());
        gameResult.setTurns(gameTurns);
        gameResult.setEventId(eventId);
        gameResultDao.insert(gameResult);
        eventTaskManager.cancelTask(eventName);
    }

    @Override
    protected Integer getGameType() {
        return MoonConstant.GUESS_EVENT_GAME;
    }
}
