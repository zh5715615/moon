package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: BullfightingUserStatisticsVo
 * @Description:
 * @date: 2026/1/13 22:16
 */
@Data
@ApiModel("斗牛用户信息统计")
public class BullfightingUserStatisticsVo {
    @ApiModelProperty("剩余局数")
    private int freeCount;

    @ApiModelProperty("今日局数")
    private int todayCount;

    @ApiModelProperty("总局数")
    private int totalCount;

    @ApiModelProperty("胜率")
    private double winRate;

    @ApiModelProperty("净积分")
    private int score;
}
