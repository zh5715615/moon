package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.beans.events.BuySpaceJediPackageEventBean;
import tcbv.zhaohui.moon.beans.events.NFTTradeOrderEventBean;
import tcbv.zhaohui.moon.beans.events.PledgeEventBean;
import tcbv.zhaohui.moon.beans.events.WithdrawEventBean;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolService
 * @Description:
 * @date: 2025/12/20 17:41
 */
public interface DappPoolService {
    void init(String contractAddress);

    String extractSpaceJediOnlyTest(Double amount) throws Exception;

    String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception;

    String cancelOrder(String owner, BigInteger tokenId) throws Exception;

    NFTTradeOrderEventBean parseTradeOrder(String txHash) throws Exception;

    PledgeEventBean parsedPledge(String txHash) throws Exception;

    WithdrawEventBean parsedWithdraw(String txHash) throws Exception;

    BuySpaceJediPackageEventBean parseBuySpaceJediPackage(String txHash) throws Exception;
}
