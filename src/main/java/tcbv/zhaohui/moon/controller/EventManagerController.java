package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.AddEventDTO;
import tcbv.zhaohui.moon.dto.EventResultDTO;
import tcbv.zhaohui.moon.dto.PageDto;
import tcbv.zhaohui.moon.entity.TbEvent;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.PageResultVo;

import javax.validation.Valid;
import java.util.Arrays;

@RestController
@RequestMapping("/game/event/manager")
public class EventManagerController {
    @PostMapping("/addEvent")
    @ApiOperation(value = "添加时事竞猜")
    public Rsp addEvent(@RequestBody @Valid AddEventDTO dto) {
        return Rsp.okData(true);
    }

    @DeleteMapping("/delEvent/{id}")
    @ApiOperation(value = "删除事件")
    public Rsp delEvent(@PathVariable("id") String id) {
        return Rsp.okData(true);
    }

    @PostMapping("/publicResult")
    @ApiOperation(value = "公布事件结果")
    public Rsp publicResult(@RequestBody @Valid EventResultDTO dto) {
        return Rsp.okData(true);
    }

    @PostMapping("/eventList")
    @ApiOperation(value = "事件列表")
    public Rsp<PageResultVo<TbEvent>> eventList(@RequestBody @Valid PageDto dto) {
        PageResultVo vo = new PageResultVo();
        return Rsp.okData(vo);
    }
}
