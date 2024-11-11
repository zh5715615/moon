package tcbv.zhaohui.moon.service.impl;

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
}
