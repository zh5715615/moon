package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 质押推广奖励表(PledgePromotion)实体类
 *
 * @author makejava
 * @since 2025-12-21 18:45:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PledgePromotionEntity {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 质押id
     */
    private String pledgeId;
    /**
     * 推广人id
     */
    private String promoterId;
    /**
     * 奖励金额
     */
    private Double rewardAmount;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 推广奖励发放hash值
     */
    private String hash;
}

