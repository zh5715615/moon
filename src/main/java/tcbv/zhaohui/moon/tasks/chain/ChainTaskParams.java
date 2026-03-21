package tcbv.zhaohui.moon.tasks.chain;

import lombok.Data;

/**
 * 存入 biz_params 字段的业务参数快照，涵盖所有 Worker 类型所需字段
 */
@Data
public class ChainTaskParams {
    /** 链上交易哈希 */
    private String txHash;
    /** 当前操作用户ID（从 JWT 快照） */
    private String userId;
    /** 当前操作钱包地址（从 JWT 快照） */
    private String address;
    /** 质押ID，用于 PLEDGE_WITHDRAW */
    private String pledgeId;
    /** NFT 订单ID，用于 NFT_TRADE_ORDER / NFT_CANCEL_ORDER */
    private Integer nftOrderId;
}
