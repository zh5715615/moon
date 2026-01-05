package tcbv.zhaohui.moon.service.chain;

import tcbv.zhaohui.moon.beans.PresaleInfoBean;
import tcbv.zhaohui.moon.beans.events.*;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.exceptions.ChainException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolService
 * @Description:
 * @date: 2025/12/20 17:41
 */
public interface DappPoolService {
    void init(EthereumService ethereumService, String contractAddress);

    String extractSpaceJediOnlyTest(BigDecimal amount) throws Exception;

    String modifyPresaleDuration(long duration) throws Exception;

    PresaleInfoBean getPackageCnt() throws ChainException;

    BigDecimal getCurrentRewardPercent(PledgeRegion region) throws ChainException;

    int getUserBuyPackageCnt() throws ChainException;

    long getPresaleTime() throws ChainException;

    NFTTradeOrderEventBean parseTradeOrder(String txHash) throws Exception;

    PledgeEventBean parsedPledge(String txHash) throws Exception;

    WithdrawEventBean parsedWithdraw(String txHash) throws Exception;

    BuySpaceJediPackageEventBean parseBuySpaceJediPackage(String txHash) throws Exception;

    SubmitOrderEventBean parseSubmitOrder(String txHash) throws Exception;

    CancelOrderEventBean parseCancelOrder(String txHash) throws Exception;
}
