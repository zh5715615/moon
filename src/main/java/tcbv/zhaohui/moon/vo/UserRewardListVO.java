package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author dawn
 * @date 2024/11/9 11:08
 */
@Data
public class UserRewardListVO implements Serializable {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("轮次")
    private Integer turns;

    @ApiModelProperty("中奖结果")
    private Integer result;

    @ApiModelProperty("是否中奖 true=是")
    private Boolean isWinning;

    @ApiModelProperty("下注金额")
    private BigDecimal amount;

    @ApiModelProperty("奖励金额")
    private BigDecimal rewardAmount;

    @ApiModelProperty("奖励时间")
    private String createTime;

    /**
     * 单双：1单 | 2双
     */
    private Integer singleAndDoubleB;
    /**
     * 涨跌：1涨 | 2跌
     */
    private Integer raseAndFallB;
    /**
     * 单双：1单 | 2双
     */
    private Integer singleAndDoubleC;
    /**
     * 涨跌：1涨 | 2跌
     */
    private Integer raseAndFallC;
    /**
     * 猜事件的id
     */
    private String eventId;
    /**
     * 猜事件的结果，枚举
     */
    private String eventResult;
}
