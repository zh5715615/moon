package tcbv.zhaohui.moon.dto;

import lombok.Data;

/**
 * 钱包登录
 *
 * @author ruoyi
 */
@Data
public class WalletLoginDto
{
    /**
     * 钱包地址
     */
    private String address;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名数据
     */
    private String dataSign;

    /**
     * 签名数据
     */
    private long timestamp;
}
