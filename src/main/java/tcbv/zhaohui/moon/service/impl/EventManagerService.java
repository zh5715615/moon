package tcbv.zhaohui.moon.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.dao.TbEventDao;
import tcbv.zhaohui.moon.dao.TbEventOptionDao;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbEventOption;
import tcbv.zhaohui.moon.service.IEventManagerService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class EventManagerService implements IEventManagerService {

    @Resource
    private TbEventDao eventDao;

    @Resource
    private TbEventOptionDao eventOptionDao;

    @Override
    @Transactional
    public TbEvent addEvent(TbEvent event, List<TbEventOption> eventOptionList) {
        event.setCreateTime(new Date());
        eventDao.insert(event);
        eventOptionDao.insertBatch(eventOptionList);
        return event;
    }

    @Override
    public boolean delEvent(String id) {
        return eventDao.deleteById(id) > 0;
    }

    @Override
    public void publicResult(String eventId, String optionId) {
        TbEvent event = new TbEvent();
        event.setId(eventId);
        event.setOptionId(optionId);
        event.setResultTime(new Date());
        eventDao.update(event);
    }

    @Override
    public Page<TbEvent> queryByPage(TbEvent tbEvent, PageRequest pageRequest) {
        long total = this.eventDao.count(tbEvent);
        return new PageImpl<>(this.eventDao.queryAllByLimit(tbEvent, pageRequest), pageRequest, total);
    }
}
