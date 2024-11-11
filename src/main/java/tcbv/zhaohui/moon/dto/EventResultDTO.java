package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("公布事件结果实体")
public class EventResultDTO {
    @ApiModelProperty(value = "事件ID")
    @NotBlank(message = "事件ID不能为空")
    private String eventId;

    @ApiModelProperty(value = "选项ID")
    @NotBlank(message = "选项ID不能为空")
    private String optionId;
}
