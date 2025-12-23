package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: TransactionDto
 * @Description:
 * @date: 2025/12/23 10:36
 */
@Data
@ApiModel("上链交易信息")
public class TransactionDto {
    @ApiModelProperty("交易哈希")
    protected String txHash;
}
