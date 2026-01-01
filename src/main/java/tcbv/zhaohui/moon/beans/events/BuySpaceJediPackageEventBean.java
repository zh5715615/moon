package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: BuySpaceJediPackageEventBean
 * @Description:
 * @date: 2025/12/24 17:30
 */
@Data
public class BuySpaceJediPackageEventBean {
    private String buyerAddress;

    private int buyCnt;

    private BigDecimal totalCost;

    private BigDecimal price;

    private int stage;
}
