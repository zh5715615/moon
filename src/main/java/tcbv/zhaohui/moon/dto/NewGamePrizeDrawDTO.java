package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author dawn
 * @date 2024/11/5 9:36
 */
@Data
public class NewGamePrizeDrawDTO implements Serializable {
    @ApiModelProperty("轮次")
    @NotBlank(message = "轮次不能为空")
    private Integer turns;
    @ApiModelProperty("投注数量")
    @NotBlank(message = "投注数量不能为空")
    private String amount;
}
