package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tb_wallet(Wallet)实体类
 *
 * @author makejava
 * @since 2025-12-03 15:10:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletEntity {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 加密密码
     */
    private String encryptPwd;
    /**
     * 私钥存储路径
     */
    private String keystorePath;
    /**
     * 创建时间
     */
    private Date createTime;
}

