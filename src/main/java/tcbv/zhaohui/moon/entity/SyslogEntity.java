package tcbv.zhaohui.moon.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统日志(SysLog)实体类
 *
 * @author makejava
 * @since 2025-12-19 13:36:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyslogEntity {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 用户IP地址
     */
    private String ip;
    /**
     * 用户浏览器信息
     */
    private String agent;
    /**
     * 操作名称
     */
    private String operation;
    /**
     * api接口路径
     */
    private String apiInterface;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 响应参数
     */
    private String responseParam;
    /**
     * HTTP状态码(如200，404，500)
     */
    private Integer httpStatus;
    /**
     * 错误信息
     */
    private Integer errorCode;
    /**
     * 错误码
     */
    private String errorMsg;
    /**
     * 模块名称
     */
    private String module;
    /**
     * duration_ms
     */
    private Long durationMs;
    /**
     * 日志时间
     */
    private Date createTime;
}

