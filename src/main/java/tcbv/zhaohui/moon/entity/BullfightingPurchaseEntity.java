package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购买斗牛次数记录表(BullfightingPurchase)实体类
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BullfightingPurchaseEntity {
    /**
     * 主键
     */
    private String id;
    /**
     * sj金额
     */
    private Double amount;
    /**
     * 次数
     */
    private Integer times;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户钱包地址
     */
    private String address;
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 创建时间
     */
    private Date createTime;
}

