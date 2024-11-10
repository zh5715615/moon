package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dawn
 * @date 2024/11/2 16:05
 */
@Data
public class AddGameOrderForDTO implements Serializable {
    @ApiModelProperty("游戏类型 1投骰子 | 2猜BNB涨跌 ")
    @NotNull(message = "游戏类型不能为空")
    private Integer gameType;
    @ApiModelProperty("参数类型 1单/涨 | 2双/跌 ")
    @NotNull(message = "参数类型")
    private Integer paramType;
    @ApiModelProperty("用户地址")
    @NotBlank(message = "用户地址不能为空")
    private String address;
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty("轮次")
    @NotNull(message = "轮次不能为空")
    private Integer turns;
    @ApiModelProperty("投注数量")
    @NotNull(message = "投注数量不能为空")
    private BigDecimal amount;
    @ApiModelProperty("交易Hash")
    private String txHash;
    @ApiModelProperty("事件id")
    private String eventId;
    @ApiModelProperty("记录id")
    private String recordId;
}
