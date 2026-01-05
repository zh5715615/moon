package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel("登录响应实体")
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    @ApiModelProperty("钱包地址")
    private String address;

    @ApiModelProperty("用户id")
    private long expired;

    @ApiModelProperty("token")
    private String token;

    @ApiModelProperty("推广链接")
    private String promoLink;

    @ApiModelProperty("父地址")
    private String parentAddress;
}
