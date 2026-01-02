package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tcbv.zhaohui.moon.enums.PledgeRegion;

/**
 * @author: zhaohui
 * @Title: PledgeRegionVo
 * @Description:
 * @date: 2026/1/1 19:03
 */
@Data
@ApiModel("质押区域信息响应体")
public class PledgeRegionVo {
    @ApiModelProperty("区域编码")
    private PledgeRegion regionCode;

    @ApiModelProperty("质押金额")
    private double pledgeAmount;

    @ApiModelProperty("到期收益百分比")
    private double pledgeRevenue;

    @ApiModelProperty("质押期限(天)")
    private int pledgePeriod;
}
