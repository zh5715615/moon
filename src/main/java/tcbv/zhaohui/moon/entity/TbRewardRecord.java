package tcbv.zhaohui.moon.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @date 2024/11/2 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbRewardRecord implements Serializable {
    /**
     * 主键
     */
    private String id;
    /**
     * 轮次
     */
    private Integer turns;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 奖励金额
     */
    private Double rewardAmount;
    /**
     * 游戏类型
     */
    private String gameType;
    /**
     * 奖励时间
     */
    private String createTime;
    /**
     * 交易合约
     */
    private String txHash;
}
