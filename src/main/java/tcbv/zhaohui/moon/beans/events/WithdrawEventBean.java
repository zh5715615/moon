package tcbv.zhaohui.moon.beans.events;

import lombok.Data;
import tcbv.zhaohui.moon.enums.PledgeRegion;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: WithdrawEventBean
 * @Description:
 * @date: 2025/12/23 10:40
 */
@Data
public class WithdrawEventBean {
    private String userAddress;

    private PledgeRegion region;

    private BigDecimal withrawAmount;
}
