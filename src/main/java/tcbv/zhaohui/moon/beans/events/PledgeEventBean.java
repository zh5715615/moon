package tcbv.zhaohui.moon.beans.events;

import lombok.Data;
import tcbv.zhaohui.moon.enums.PledgeRegion;

import java.math.BigDecimal;

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

    private BigDecimal pledgeAmount;
}
