package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: zhaohui
 * @Title: PresaleInfoVo
 * @Description:
 * @date: 2025/12/31 18:53
 */
@Data
@ApiModel("预售进度信息响应实体")
public class PresaleInfoVo {
    @ApiModelProperty("总套参数")
    private Integer total;

    @ApiModelProperty("已售套参数")
    private Integer sold;

    @ApiModelProperty("已售百分比")
    private Double soldPercent;

    @ApiModelProperty("当前轮次信息")
    private CurrentRoundVo currentRound;

    @ApiModelProperty("下一轮信息")
    private AfterRoundVo afterRound;

    @ApiModelProperty("轮次信息")
    private List<RoundsVo> rounds;

    @Data
    public static class CurrentRoundVo {
        @ApiModelProperty("轮次")
        private Integer round;

        @ApiModelProperty("价格")
        private Double price;

        @ApiModelProperty("一套花费USDT价钱")
        private Double cost;

        @ApiModelProperty("已售出数量")
        private Integer sold;

        @ApiModelProperty("总数量")
        private Integer total;
    }

    @Data
    public static class AfterRoundVo {
        @ApiModelProperty("轮次")
        private Integer round;

        @ApiModelProperty("价格")
        private Double price;

        @ApiModelProperty("一套花费USDT价钱")
        private Double cost;

        @ApiModelProperty("总数量")
        private Integer total;

        @ApiModelProperty("涨价百分比")
        private Double increasePercentage;
    }

    @Data
    public static class RoundsVo {
        @ApiModelProperty("轮次")
        private Integer round;

        @ApiModelProperty("价格")
        private Double price;

        @ApiModelProperty("已售套数(未开始则为0)")
        private Integer sold;

        @ApiModelProperty("总套数")
        private Integer total;

        @ApiModelProperty("已结束，进行中，未开始")
        private String status;
    }
}
