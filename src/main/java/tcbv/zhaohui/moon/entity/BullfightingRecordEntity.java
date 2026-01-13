package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 斗牛游戏记录(BullfightingRecord)实体类
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BullfightingRecordEntity {
    /**
     * 主键id
     */
    private String id;
    /**
     * 押注积分
     */
    private Integer score;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 胜负（true/false）
     */
    private Boolean winlose;
    /**
     * 自己的牌
     */
    private String selfCard;
    /**
     * 其他人的牌
     */
    private String otherCard;
    /**
     * 记录时间
     */
    private Date createTime;
}

