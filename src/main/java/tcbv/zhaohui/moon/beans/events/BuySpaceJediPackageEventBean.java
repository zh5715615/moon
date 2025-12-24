package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

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

    private Double totalCost;

    private Double price;

    private int stage;
}
