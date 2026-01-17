package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: BuyGameTimesEventBean
 * @Description:
 * @date: 2026/1/17 11:23
 */
@Data
public class BuyGameTimesEventBean {
    private String userAddress;

    private BigDecimal amount;

    private int times;
}
