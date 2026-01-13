package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 斗牛用户积分(BullfightingScore)实体类
 *
 * @author makejava
 * @since 2026-01-13 19:38:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BullfightingScoreEntity {
    /**
     * 主键id
     */
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 积分
     */
    private Integer score;
    /**
     * 剩余次数
     */
    private Integer times;
    /**
     * 游戏日期
     */
    private Date gameDate;
}

