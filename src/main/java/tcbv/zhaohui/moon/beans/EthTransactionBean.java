package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 交易信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EthTransactionBean extends TransactionBean {
    private String toAddress; //eth接收地址

    private double amount; //交易金额
}
