package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author dawn
 * @date 2024/11/5 9:39
 */
@Data
@ApiModel("添加的事件对象")
public class AddEventDTO implements Serializable {
    @ApiModelProperty(value = "事件名称")
    @NotBlank(message = "事件名称不能为空")
    private String name;

    @ApiModelProperty(value = "事件说明")
    private String content;

    @ApiModelProperty(value = "投注开始时间")
    @NotNull(message = "投注开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date betTime;

    @ApiModelProperty(value = "开奖时间")
    @NotNull(message = "开奖时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date resultTime;

    @ApiModelProperty(value = "事件选项")
    @NotEmpty(message = "事件选项不能为空")
    private List<String> options;
}
