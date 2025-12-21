package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

/**
 * @author: zhaohui
 * @Title: PledgeEventBean
 * @Description:
 * @date: 2025/12/21 11:04
 */
@Data
public class PledgeEventBean {
    private String userAddress;

    private PledgeRegion region;

    private Double pledgeAmount;
}
