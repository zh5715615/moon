package tcbv.zhaohui.moon.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zhaohui
 * @Title: PledgeHistoryVo
 * @Description:
 * @date: 2026/1/2 19:24
 */
@Data
@ApiModel("全站动态响应实体")
public class PledgeHistoryVo implements Serializable {
    @ApiModelProperty("交易hash值")
    private String hash;

    @ApiModelProperty("质押用户地址")
    private String address;

    @ApiModelProperty("质押时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pledgeDatetime;

    @ApiModelProperty("质押收益百分比")
    private Double pledgeRevenue;

    @ApiModelProperty("质押金额")
    private Double pledgeAmount;

    @ApiModelProperty("质押天数")
    private Integer pledgePeriod;
}
