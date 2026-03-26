package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("异步任务状态响应实体")
public class TaskStatusVo {
    @ApiModelProperty("任务ID")
    private String taskId;
    @ApiModelProperty("任务状态：PENDING / PROCESSING / SUCCESS / FAILED")
    private String status;
    @ApiModelProperty("失败原因，仅 FAILED 时有值")
    private String errorMsg;

    private static final String[] STATUS_NAMES = {"PENDING", "PROCESSING", "SUCCESS", "FAILED"};

    public static TaskStatusVo of(String id, int statusCode, String errorMsg) {
        TaskStatusVo vo = new TaskStatusVo();
        vo.setTaskId(id);
        vo.setStatus(statusCode >= 0 && statusCode < STATUS_NAMES.length ? STATUS_NAMES[statusCode] : "UNKNOWN");
        vo.setErrorMsg(errorMsg);
        return vo;
    }
}
