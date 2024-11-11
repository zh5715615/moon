package tcbv.zhaohui.moon.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TbEventOption)实体类
 *
 * @author makejava
 * @since 2024-11-11 16:34:02
 */
@Data
@ApiModel("事件选项")
public class TbEventOption implements Serializable {
    private static final long serialVersionUID = -18393601082310207L;
/**
     * 主键id
     */
    @ApiModelProperty(value = "选项id")
    private String id;
/**
     * 事件id
     */
    @ApiModelProperty(value = "事件id")
    private String eventId;
/**
     * 选项
     */
    @ApiModelProperty(value = "事件选项")
    private String option;
/**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
/**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}

