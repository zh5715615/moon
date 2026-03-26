package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: BullfightingRankingVo
 * @Description:
 * @date: 2026/1/14 16:56
 */
@Data
@ApiModel(description = "斗牛排名单体信息")
public class BullfightingRankingItemVo {
    @ApiModelProperty("排名")
    private int rank;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("用户地址")
    private String address;

    @ApiModelProperty("当日积分")
    private int score;

    @ApiModelProperty("是否是本人")
    private boolean self;
}
