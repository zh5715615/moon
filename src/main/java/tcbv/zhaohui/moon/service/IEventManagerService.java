package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbEventOption;

import java.util.List;

public interface IEventManagerService {
    TbEvent addEvent(TbEvent event, List<TbEventOption> eventOptionList);

    boolean delEvent(String id);
}
