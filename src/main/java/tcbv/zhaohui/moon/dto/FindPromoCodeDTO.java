package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author dawn
 * @date 2024/11/14 10:09
 */
@Data
public class FindPromoCodeDTO implements Serializable {

    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不能为空")
    private String userId;
}
