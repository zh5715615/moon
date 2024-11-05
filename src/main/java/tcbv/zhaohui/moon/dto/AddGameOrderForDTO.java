package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dawn
 * @date 2024/11/2 16:05
 */
@Data
public class AddGameOrderForDTO implements Serializable {
    @ApiModelProperty("游戏类型 1投骰子 | 2猜BNB涨跌 | 3猜事件")
    @NotBlank(message = "游戏类型不能为空")
    private Integer gameType;
    @ApiModelProperty("参数类型 1单/涨 | 2双/跌 ")
    @NotBlank(message = "参数类型")
    private Integer paramType;
    @ApiModelProperty("用户地址")
    @NotBlank(message = "用户地址不能为空")
    private String address;
    @ApiModelProperty("轮次")
    @NotBlank(message = "轮次不能为空")
    private Integer turns;
    @ApiModelProperty("投注数量")
    @NotBlank(message = "投注数量不能为空")
    private String amount;
    @ApiModelProperty("交易Hash")
    private String txHash;

}
