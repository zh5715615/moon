package tcbv.zhaohui.moon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 钱包登录
 *
 * @author ruoyi
 */
@Data
@ApiModel("登录实体")
public class WalletLoginDto
{
    /**
     * 钱包地址
     */
    @ApiModelProperty("用户地址")
    @NotBlank(message = "用户地址不能为空")
    private String address;

    /**
     * 签名
     */
    @ApiModelProperty("签名")
    @NotBlank(message = "签名不能为空")
    private String sign;

    /**
     * 签名数据
     */
    @ApiModelProperty("签名数据")
    @NotBlank(message = "签名数据不能为空")
    private String dataSign;

    /**
     * 签名数据
     */
    @ApiModelProperty("时间戳")
    private long timestamp;
}
