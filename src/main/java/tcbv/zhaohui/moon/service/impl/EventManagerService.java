package tcbv.zhaohui.moon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.*;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbEventOption;
import tcbv.zhaohui.moon.scheduled.GuessEventBetScheduled;
import tcbv.zhaohui.moon.scheduled.GuessEventDrawScheduled;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.IEventManagerService;
import tcbv.zhaohui.moon.service.IEventTaskManager;
import tcbv.zhaohui.moon.service.IMoonBaseService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EventManagerService implements IEventManagerService {

    @Resource
    private TbEventDao eventDao;

    @Resource
    private TbEventOptionDao eventOptionDao;

    @Autowired
    private IEventTaskManager eventTaskManager;

    @Resource
    private TbGameResultDao gameResultDao;

    @Autowired
    private IMoonBaseService moonBaseService;

    @Resource
    private TbTxRecordDao txRecordDao;

    @Resource
    TbRewardRecordDao rewardRecordDao;

    @Resource
    private TbUserDao userDao;

    public static final String BET = "-bet";

    public static final String DRAW = "-draw";

    @Override
    @Transactional
    public TbEvent addEvent(TbEvent event, List<TbEventOption> eventOptionList) {
        event.setCreateTime(new Date());
        event.setStatus(MoonConstant.EVENT_WAITING);
        String eventTaskName = event.getName() + BET;
        LocalDateTime localDateTime = event.getBetTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        eventTaskManager.addTask(eventTaskName, new GuessEventBetScheduled(eventTaskManager, gameResultDao, eventDao, eventTaskName, event.getId()), localDateTime);
        eventDao.insert(event);
        eventOptionDao.insertBatch(eventOptionList);
        return event;
    }

    @Override
    public boolean delEvent(String id) {
        TbEvent event = eventDao.queryById(id);
        Date now = new Date();
        if (event.getBetTime().after(now)) {
            eventTaskManager.cancelTask(event.getName());
        } else {
            throw new RuntimeException("已经开始投注，不能删除");
        }
        return eventDao.deleteById(id) > 0;
    }

    @Override
    public void publicResult(String eventId, String optionId) {
        TimerMaps.stopGuessEvent();
        TbEvent event = new TbEvent();
        event.setId(eventId);
        event.setOptionId(optionId);
        event.setResultTime(new Date());
        String eventTaskName = event.getName() + DRAW;
        LocalDateTime localDateTime = event.getResultTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusSeconds(10);
        eventTaskManager.addTask(eventTaskName, new GuessEventDrawScheduled(moonBaseService, userDao, txRecordDao, rewardRecordDao, eventDao, eventTaskManager, gameResultDao, eventTaskName, eventId, optionId), localDateTime);
        eventDao.update(event);
    }

    @Override
    public Page<TbEvent> queryByPage(TbEvent tbEvent, PageRequest pageRequest) {
        long total = this.eventDao.count(tbEvent);
        Pageable pageable = PageRequest.of(pageRequest.getPageNumber() - 1, pageRequest.getPageSize(), Sort.by(Sort.Direction.DESC, "create_time"));
        return new PageImpl<>(this.eventDao.queryAllByLimit(tbEvent, pageable), pageRequest, total);
    }

    @Override
    public TbEvent queryById(String id) {
        return eventDao.queryById(id);
    }

    @Override
    public List<TbEventOption> queryOptionById(String eventId) {
        return eventOptionDao.queryByEventId(eventId);
    }

    private void startScheduled(List<TbEvent> eventList) {
        for (TbEvent event : eventList) {
            if(Objects.equals(event.getStatus(), MoonConstant.EVENT_WAITING) &&
                event.getBetTime().after(new Date())) {
                String eventTaskName = event.getName() + BET;
                LocalDateTime localDateTime = event.getBetTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                eventTaskManager.addTask(eventTaskName, new GuessEventBetScheduled(eventTaskManager, gameResultDao, eventDao, eventTaskName, event.getId()), localDateTime);
            }
        }
    }

    @Override
    public void init() {
        Page<TbEvent> tbEventPage = queryByPage(new TbEvent(), PageRequest.of(1, 100));
        if (tbEventPage.getTotalElements() > 0) {
            startScheduled(tbEventPage.toList());
            int pageTotal = tbEventPage.getTotalPages();
            for (int i = 2; i < pageTotal; i++) {
                tbEventPage = queryByPage(new TbEvent(), PageRequest.of(i, 100));
                startScheduled(tbEventPage.toList());
            }
        }
    }
}
