package tcbv.zhaohui.moon.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 质押表(Pledge)实体类
 *
 * @author makejava
 * @since 2025-12-21 11:01:17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PledgeEntity {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 质押区域
     */
    private Integer region;
    /**
     * 质押金额
     */
    private Double amount;
    /**
     * 奖励金额
     */
    private Double withdrawAmount;
    /**
     * 到期时间
     */
    private Date expireTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 提取时间
     */
    private Date withdrawTime;
    /**
     * 质押交易hash值
     */
    private String pledgeHash;
    /**
     * 提取交易hash值
     */
    private String withdrawHash;
}

