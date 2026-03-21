package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.service.ChainTxTaskService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.TaskStatusVo;

@RestController
@RequestMapping("/api/v1/moon/task")
@Api(tags = "异步任务状态")
public class TaskStatusController {

    @Autowired
    private ChainTxTaskService chainTxTaskService;

    @GetMapping("/status")
    @ApiOperation("查询异步任务处理状态")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> status(@RequestParam("taskId") String taskId) {
        ChainTxTaskEntity task = chainTxTaskService.queryById(taskId);
        if (task == null) {
            // 任务不存在说明已被删除（SUCCESS 后删除）
            return Rsp.okData(TaskStatusVo.of(taskId, 2, null));
        }
        return Rsp.okData(TaskStatusVo.of(task.getId(), task.getStatus(), task.getErrorMsg()));
    }
}
