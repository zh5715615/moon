package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author: zhaohui
 * @Title: WithdrawDto
 * @Description:
 * @date: 2025/12/23 10:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("提取质押奖励请求体")
public class WithdrawDto extends TransactionDto{
    @ApiModelProperty("质押ID")
    @NotBlank(message = "质押ID不能为空")
    private String pledgeId;
}
