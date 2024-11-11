package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tcbv.zhaohui.moon.entity.TbEventOption;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("事件详情")
public class EventVo {
    @ApiModelProperty(value = "事件id")
    private String id;
    @ApiModelProperty(value = "事件名称")
    private String name;
    @ApiModelProperty(value = "事件内容")
    private String content;
    @ApiModelProperty(value = "事件结果")
    private String optionId;
    @ApiModelProperty(value = "事件创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新内容时间")
    private Date updateTime;
    @ApiModelProperty(value = "开始投注时间")
    private Date betTime;
    @ApiModelProperty(value = "事件结果时间(开奖时间)")
    private Date resultTime;
    @ApiModelProperty(value = "事件选项")
    private List<TbEventOption> eventOptionList;
}
