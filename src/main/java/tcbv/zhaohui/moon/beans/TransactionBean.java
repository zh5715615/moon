package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 交易信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBean {
    private String fromAddress; //发送地址

    private String txHash; //交易hash

    private long blockHeight; //所在区块高度

    private long timestamp; //时间戳
}
