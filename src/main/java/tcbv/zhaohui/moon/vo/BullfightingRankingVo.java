package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: zhaohui
 * @Title: BullfightingRankingVo
 * @Description:
 * @date: 2026/1/14 16:56
 */
@Data
@ApiModel(description = "斗牛排名单体信息")
public class BullfightingRankingVo {
    @ApiModelProperty("今日奖池")
    private Double todayPrizePool;

    @ApiModelProperty("我的排名")
    private int ranking;

    @ApiModelProperty("我的积分")
    private int score;

    @ApiModelProperty("奖励")
    private int reward;

    @ApiModelProperty("排名")
    private List<BullfightingRankingItemVo> rankingList;
}
