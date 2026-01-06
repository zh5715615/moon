package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author: zhaohui
 * @Title: AdminLoginDto
 * @Description:
 * @date: 2026/1/6 16:07
 */
@Data
@ApiModel("管理员登录实体")
public class AdminLoginDto {
    @ApiModelProperty("用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码不能为空")
    private String password;
}
