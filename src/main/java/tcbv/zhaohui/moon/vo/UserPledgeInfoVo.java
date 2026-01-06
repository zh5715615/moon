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
 * @Title: UserPledgeInfoVo
 * @Description:
 * @date: 2026/1/1 19:39
 */
@Data
@ApiModel("用户质押信息响应实体")
public class UserPledgeInfoVo {
    @ApiModelProperty("质押id")
    private String pledgeId;

    @ApiModelProperty("质押区域")
    private String regionCode;

    @ApiModelProperty("质押到期时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    @ApiModelProperty("质押到期收益（SJ）")
    private Double pledgeRevenueAmount;

    @ApiModelProperty("质押本金（锁定金额）")
    private Double pledgeAmount;

    @ApiModelProperty("质押时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pledgeDatetime;

    @ApiModelProperty("质押期限（30，60，90，180）")
    private Integer pledgePeriod;

    @ApiModelProperty("质押到期收益百分比")
    private Double pledgeRevenuePercent;

    @ApiModelProperty("进度百分比")
    private Double progress;
}
