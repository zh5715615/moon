package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 斗牛游戏领取奖励记录(BullfightingReward)实体类
 *
 * @author makejava
 * @since 2026-01-17 11:49:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BullfightingRewardEntity {
    /**
     * 主键
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
     * 奖励金额
     */
    private Double amount;
    /**
     * 游戏日期
     */
    private Date gameDate;
    /**
     * 交易hash
     */
    private String hash;
    /**
     * 领取时间
     */
    private Date createTime;
}

