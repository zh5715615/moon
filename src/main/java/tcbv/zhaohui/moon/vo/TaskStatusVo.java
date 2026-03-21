package tcbv.zhaohui.moon.vo;

import lombok.Data;

@Data
public class TaskStatusVo {
    private String taskId;
    /** PENDING / PROCESSING / SUCCESS / FAILED */
    private String status;
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
