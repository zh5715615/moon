package tcbv.zhaohui.moon.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @date 2024/11/2 14:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbGameResult implements Serializable {
    /**
     * 开奖结果主键id
     */
    private String id;
    /**
     * 游戏类型：1投骰子 | 2猜BNB涨跌 | 3猜事件
     */
    private Integer gameType;
    /**
     * 单双：1单 | 2双
     */
    private Integer singleAndDouble;
    /**
     * 涨跌：1涨 | 2跌
     */
    private Integer raseAndFall;
    /**
     * 猜事件的id
     */
    private String eventId;
    /**
     * 猜事件的结果，枚举
     */
    private String eventResult;
    /**
     * 轮次
     */
    private Integer turns;
    /**
     * 是否开奖
     */
    private Integer isDrawn;
}
