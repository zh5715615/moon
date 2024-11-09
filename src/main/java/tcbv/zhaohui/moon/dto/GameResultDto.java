package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("游戏开奖查询条件")
public class GameResultDto extends PageDto {
    @ApiModelProperty(value = "游戏类型 1投骰子 | 2猜BNB涨跌 | 3猜事件")
    @NotNull(message = "游戏类型不能为空")
    private Integer gameType;

    @ApiModelProperty(value = "轮次")
    @NotNull(message = "轮次不能为空")
    private Integer turns;
}
