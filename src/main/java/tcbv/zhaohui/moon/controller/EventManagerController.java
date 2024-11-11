package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.AddEventDTO;
import tcbv.zhaohui.moon.dto.EventResultDTO;
import tcbv.zhaohui.moon.dto.PageDto;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.entity.TbEventOption;
import tcbv.zhaohui.moon.service.IEventManagerService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.PageResultVo;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/game/event/manager")
public class EventManagerController {

    @Autowired
    private IEventManagerService eventManagerService;

    @PostMapping("/addEvent")
    @ApiOperation(value = "添加时事竞猜")
    public Rsp<TbEvent> addEvent(@RequestBody @Valid AddEventDTO dto) {
        TbEvent event = new TbEvent();
        BeanUtils.copyProperties(dto, event);
        if (dto.getOptions().size() < 2) {
            return Rsp.error("选项数量必须大于等于2");
        }
        String eventId = UUID.randomUUID().toString();
        event.setId(eventId);
        List<TbEventOption> eventOptionList = new ArrayList<>();
        for (String option : dto.getOptions()) {
            TbEventOption eventOption = new TbEventOption();
            eventOption.setEventId(eventId);
            eventOption.setId(UUID.randomUUID().toString());
            eventOption.setOption(option);
            eventOption.setCreateTime(new Date());
            eventOptionList.add(eventOption);
        }
        event = eventManagerService.addEvent(event, eventOptionList);
        return Rsp.okData(event);
    }

    @DeleteMapping("/delEvent/{id}")
    @ApiOperation(value = "删除事件")
    public Rsp delEvent(@PathVariable("id") String id) {
        return Rsp.okData(eventManagerService.delEvent(id));
    }

    @PostMapping("/publicResult")
    @ApiOperation(value = "公布事件结果")
    public Rsp publicResult(@RequestBody @Valid EventResultDTO dto) {
//        eventManagerService.publicResult(dto.getEventId(), dto.getOptionId());
        return Rsp.okData(true);
    }

    @PostMapping("/eventList")
    @ApiOperation(value = "事件列表")
    public Rsp<PageResultVo<TbEvent>> eventList(@RequestBody @Valid PageDto dto) {
        PageResultVo vo = new PageResultVo();
        return Rsp.okData(vo);
    }
}
