package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: zhaohui
 * @Title: BullfightingStartVo
 * @Description:
 * @date: 2026/1/12 21:48
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel("斗牛开始响应实体")
public class BullfightingStartVo implements Serializable {
    @ApiModelProperty("本人牌局信息")
    @JsonProperty("self")
    private UserCardVo self;
    @ApiModelProperty("本人当前积分")
    @JsonProperty("userScores")
    private Integer userScores;
    @ApiModelProperty("剩余局数")
    @JsonProperty("remainingTimes")
    private Integer remainingTimes;
    @ApiModelProperty("本局押注积分")
    @JsonProperty("roundOfScore")
    private int roundOfScore;
    @ApiModelProperty("其他玩家牌局信息")
    @JsonProperty("other")
    private List<UserCardVo> other;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    @ApiModel("斗牛玩家牌局信息")
    public static class UserCardVo implements Serializable {
        @ApiModelProperty("玩家地址")
        @JsonProperty("address")
        private String address;
        @ApiModelProperty("手牌（逗号分隔）")
        @JsonProperty("playingCards")
        private String playingCards;
        @ApiModelProperty("牛几")
        @JsonProperty("bullfighting")
        private int bullfighting;
        @ApiModelProperty("是否获胜")
        @JsonProperty("win")
        private Boolean win = Boolean.FALSE;
    }
}
