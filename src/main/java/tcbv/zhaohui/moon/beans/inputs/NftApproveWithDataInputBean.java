package tcbv.zhaohui.moon.beans.inputs;

import lombok.Data;

/**
 * @author: zhaohui
 * @Title: NftApproveWithDataInputBean
 * @Description:
 * @date: 2025/12/23 22:24
 */
@Data
public class NftApproveWithDataInputBean {
    private String toAddress;

    private String tokenId;

    private Double price;
}
