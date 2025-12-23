package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

/**
 * @author: zhaohui
 * @Title: NFTTradeOrderBean
 * @Description:
 * @date: 2025/12/23 20:39
 */
@Data
public class NFTTradeOrderEventBean {
    private String tokenId;

    private String sellerAddress;

    private String buyerAddress;

    private Double price;
}
