package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author dawn
 * @date 2024/11/14 10:15
 */
@Data
public class ConfirmPromoCodeDTO implements Serializable {
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
    @ApiModelProperty("推广码")
    @NotNull(message = "推广码不能为空")
    private Integer promoCode;
}
