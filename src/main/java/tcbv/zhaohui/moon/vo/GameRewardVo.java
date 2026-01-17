package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: GameRewardVo
 * @Description:
 * @date: 2026/1/17 11:54
 */
@Data
@ApiModel("游戏奖励响应体")
public class GameRewardVo {
    @ApiModelProperty("昨日奖励")
    private Double yesterdayReward;

    @ApiModelProperty("今日奖励")
    private Double todayReward;
}
