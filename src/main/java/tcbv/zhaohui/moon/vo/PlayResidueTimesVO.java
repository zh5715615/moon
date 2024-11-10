package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dawn
 * @date 2024/11/2 15:55
 */
@Data
public class PlayResidueTimesVO implements Serializable {
    @ApiModelProperty("当前游戏类型剩余时间，小于60秒都返回false")
    private Boolean isOk;
    @ApiModelProperty("当前轮次")
    private Integer turns;
    @ApiModelProperty("游戏类型 1投骰子 | 2猜BNB涨跌 | 3猜事件")
    private Integer gameType;

}