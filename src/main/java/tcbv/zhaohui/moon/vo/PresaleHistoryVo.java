package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhaohui
 * @Title: PresaleHistoryVo
 * @Description:
 * @date: 2026/1/1 15:26
 */
@Data
@ApiModel("预售历史记录响应体")
public class PresaleHistoryVo implements Serializable {
    @ApiModelProperty("交易hash")
    private String hash;

    @ApiModelProperty("轮次")
    private Integer round;

    @ApiModelProperty("购买套数")
    private Integer count;

    @ApiModelProperty("用户地址")
    private String address;

    @ApiModelProperty("购买价钱（U）")
    private Double cost;

    @ApiModelProperty("购买时间")
    private Date datetime;
}
