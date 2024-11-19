package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (TbNftReward)实体类
 *
 * @author makejava
 * @since 2024-11-19 20:14:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbNftReward implements Serializable {
    private static final long serialVersionUID = -85372511042513258L;
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 用户地址
     */
    private String userAddress;
    /**
     * 勋章数量
     */
    private Integer nftAmount;
    /**
     * 奖励金额
     */
    private Double rewardAmount;
    /**
     * 交易hash
     */
    private String txHash;
    /**
     * 周期类型：1周 | 2月
     */
    private Integer cycle;
    /**
     * 创建时间
     */
    private Date createTime;

    public TbNftReward(Integer cycle) {
        this.cycle = cycle;
    }
}

