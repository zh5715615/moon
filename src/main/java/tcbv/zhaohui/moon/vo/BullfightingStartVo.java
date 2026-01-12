package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BullfightingStartVo implements Serializable {
    @JsonProperty("self")
    private UserCardVo self;
    @JsonProperty("userScores")
    private Integer userScores;
    @JsonProperty("remainingTimes")
    private Integer remainingTimes;
    @JsonProperty("roundOfScore")
    private String roundOfScore;
    @JsonProperty("other")
    private List<UserCardVo> other;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class UserCardVo implements Serializable {
        @JsonProperty("address")
        private String address;
        @JsonProperty("playingCards")
        private String playingCards;
        @JsonProperty("bullfighting")
        private int bullfighting;
        @JsonProperty("win")
        private Boolean win = Boolean.FALSE;
    }
}
