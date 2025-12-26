package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: zhaohui
 * @Title: KeyInfoDto
 * @Description:
 * @date: 2025/12/26 11:18
 */
@Data
@ApiModel("用户私钥信息")
public class KeyInfoDto {
    @ApiModelProperty("助记词")
    @NotBlank(message = "助记词不能为空")
    private String mnemonic;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
