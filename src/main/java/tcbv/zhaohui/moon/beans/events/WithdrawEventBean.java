package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

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

    private Double withrawAmount;
}
