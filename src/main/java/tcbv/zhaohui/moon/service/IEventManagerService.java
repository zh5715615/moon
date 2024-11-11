package tcbv.zhaohui.moon.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbEventOption;

import java.util.List;

public interface IEventManagerService {
    TbEvent addEvent(TbEvent event, List<TbEventOption> eventOptionList);

    boolean delEvent(String id);

    void publicResult(String eventId, String optionId);

    /**
     * 分页查询
     *
     * @param tbEvent 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TbEvent> queryByPage(TbEvent tbEvent, PageRequest pageRequest);

    TbEvent queryById(String id);

    List<TbEventOption> queryOptionById(String id);
}
