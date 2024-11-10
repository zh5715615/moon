package tcbv.zhaohui.moon.entity;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @date 2024/11/2 14:47
 */@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbTxRecord  implements Serializable {
    /** 主键， uuid */
    private String id ;
    /** 用户id */
    private String userId ;
    /** 游戏类型：1投骰子 | 2猜BNB涨跌 | 3猜事件 */
    private Integer gameType ;
    /** 单双：1单 | 2双 */
    private Integer singleAndDouble ;
    /** 涨跌：1涨 | 2跌 */
    private Integer raseAndFall ;
    /** 猜事件的id */
    private String eventId ;
    /** 猜事件的结果，枚举 */
    private String eventResult ;
    /** 交易hash */
    private String txHash ;
    /** 轮次 */
    private Integer turns ;
    /** 创建时间 */
    private Date createTime ;
    /** 投注数量 */
    private BigDecimal amount ;
}
