package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

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
    @NotBlank(message = "交易hash不能为空")
    protected String txHash;
}
