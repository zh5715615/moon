package tcbv.zhaohui.moon.service.chain;

import tcbv.zhaohui.moon.beans.events.*;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolService
 * @Description:
 * @date: 2025/12/20 17:41
 */
public interface DappPoolService {
    void init(EthereumService ethereumService, String contractAddress);

    String extractSpaceJediOnlyTest(Double amount) throws Exception;

    String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception;

    String cancelOrder(String owner, BigInteger tokenId) throws Exception;

    NFTTradeOrderEventBean parseTradeOrder(String txHash) throws Exception;

    PledgeEventBean parsedPledge(String txHash) throws Exception;

    WithdrawEventBean parsedWithdraw(String txHash) throws Exception;

    BuySpaceJediPackageEventBean parseBuySpaceJediPackage(String txHash) throws Exception;

    SubmitOrderEventBean parseSubmitOrder(String txHash) throws Exception;

    CancelOrderEventBean parseCancelOrder(String txHash) throws Exception;
}
