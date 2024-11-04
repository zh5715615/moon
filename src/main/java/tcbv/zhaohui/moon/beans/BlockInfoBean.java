package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 区块信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockInfoBean {
    private String blockHash; //区块hash

    private long blockHeigh; //区块高度

    private long timestamp;  //区块时间戳

    private long txCount; //交易数量

    private String toAddress; //trx接收地址

    private double amount; //交易金额

    private List<String> txHashList; //交易hash列表
}
