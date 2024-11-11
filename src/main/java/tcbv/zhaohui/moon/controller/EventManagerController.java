package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.AddEventDTO;
import tcbv.zhaohui.moon.dto.EventResultDTO;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.validation.Valid;

@RestController
@RequestMapping("/game/event/manager")
public class EventManagerController {
    @PostMapping("/addEvent")
    @ApiOperation(value = "添加时事竞猜")
    public Rsp addEvent(@RequestBody @Valid AddEventDTO dto) {
        return Rsp.okData(true);
    }

    @DeleteMapping("/delEvent/{id}")
    @ApiOperation(value = "添加时事竞猜")
    public Rsp delEvent(@PathVariable("id") String id) {
        return Rsp.okData(true);
    }

    @PostMapping("/publicResult")
    @ApiOperation(value = "公布事件结果")
    public Rsp publicResult(@RequestBody @Valid EventResultDTO dto) {
        return Rsp.okData(true);
    }
}
