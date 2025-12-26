package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zhaohui
 * @Title: KeyInfoCipherVo
 * @Description:
 * @date: 2025/12/26 11:39
 */
@Data
@ApiModel("密钥信息")
public class KeyInfoCipherVo {
    @ApiModelProperty("私钥文件内容(base64)")
    private String keystoreContent;

    @ApiModelProperty("钱包地址")
    private String address;

    @ApiModelProperty("加密密码")
    private String encryptPwd;
}
