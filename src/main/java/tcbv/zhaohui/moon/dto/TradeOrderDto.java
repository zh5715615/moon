package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: zhaohui
 * @Title: TradeOrderDto
 * @Description:
 * @date: 2025/12/23 20:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("订单成交请求入参实体")
public class TradeOrderDto extends TransactionDto{
    @ApiModelProperty("nft订单id")
    private Integer nftOrderId;
}
