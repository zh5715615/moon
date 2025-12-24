package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SJ套餐包交易(SjPackageTx)实体类
 *
 * @author makejava
 * @since 2025-12-24 20:09:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SjPackageTxEntity {
    /**
     * 交易主键
     */
    private Integer id;
    /**
     * 单价
     */
    private Double price;
    /**
     * 阶段
     */
    private Integer stage;
    /**
     * 买家用户id
     */
    private String buyerId;
    /**
     * 交易hash值
     */
    private String hash;
    /**
     * 用户钱包地址
     */
    private String address;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 买入的包的套数
     */
    private Integer pacakgeCnt;
}

