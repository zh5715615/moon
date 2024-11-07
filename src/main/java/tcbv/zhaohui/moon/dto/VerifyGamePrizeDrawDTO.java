package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author dawn
 * @date 2024/11/5 9:48
 */
@Data
public class VerifyGamePrizeDrawDTO implements Serializable {
    @ApiModelProperty("游戏类型 1投骰子 | 2猜BNB涨跌 ")
    @NotNull(message = "游戏类型不能为空")
    private Integer gameType;
    @ApiModelProperty("轮次")
    @NotNull(message = "轮次不能为空")
    private Integer turns;
}
