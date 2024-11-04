package tcbv.zhaohui.moon.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @date : 2024-11-2
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbEvent implements Serializable {
    /**
     * 事件id
     */
    private Integer id;
    /**
     * 事件内容
     */
    private String content;
    /**
     * 事件结果
     */
    private String result;
    /**
     * 事件发起时间
     */
    private Date createTime;
    /**
     * 更新时间内容时间
     */
    private Date updateTime;
    /**
     * 事件结果时间
     */
    private Date resultTime;
    /**
     * 计算状态;1有效 | 2注销 | 3已结算
     */
    private Integer status;
}
