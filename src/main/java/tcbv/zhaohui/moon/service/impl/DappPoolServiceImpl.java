package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.events.PledgeEventBean;
import tcbv.zhaohui.moon.beans.events.PledgeRegion;
import tcbv.zhaohui.moon.beans.events.WithdrawEventBean;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceImpl
 * @Description:
 * @date: 2025/12/20 17:48
 */
@Service
@Slf4j
public class DappPoolServiceImpl extends EthereumService implements DappPoolService {
    private DappPool dappPool;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private ICardNFTTokenService cardNFTTokenService;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        dappPool = DappPool.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    @Web3TxGuard
    public String extractSpaceJediOnlyTest(Double amount) throws Exception {
        BigInteger amountWei = EthMathUtil.doubleToBigInteger(amount, spaceJediService.getDecimals());
        return dappPool.extractSpaceJediOnlyTest(amountWei).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception {
        BigInteger priceWei = EthMathUtil.doubleToBigInteger(price, spaceJediService.getDecimals());
        boolean exists = cardNFTTokenService.exists(tokenId.toString(10));
        if (!exists) {
            log.error("tokenId {} not exists", tokenId);
            return null;
        }
        return dappPool.submitOrder(seller, tokenId, priceWei).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String cancelOrder(String owner, BigInteger tokenId) throws Exception {
        return dappPool.cancelOrder(owner, tokenId).send().getTransactionHash();
    }

    @Override
    public void parseTradeOrder(String txHash) {
        try {
            AbiInputDecoder.DecodedCall call = AbiInputDecoder.decodeTxHash(web3j, txHash, DappPool.ABI_JSON);
            if (call.getFunctionName().equals(DappPool.FUNC_TRADEORDER)) {
                log.info("Input Args: {}", GsonUtil.toJson(call.getArgs()));
            }

            List<AbiEventLogDecoder.DecodedEvent> events =
                    AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON);
            for (AbiEventLogDecoder.DecodedEvent event : events) {
                if (event.getEventName().equals(DappPool.TRADEORDER_EVENT.getName())) {
                    log.info("Log Args: {}", GsonUtil.toJson(event.getArgs()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PledgeEventBean parsedPledge(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.PLEDGE_EVENT);
        if (event == null) {
            return null;
        }
        PledgeEventBean pledgeEventBean = new PledgeEventBean();
        BigInteger pledgeAmountWei = (BigInteger) event.getArgs().get("amount");
        Double pledgeAmount = EthMathUtil.bigIntegerToDouble(pledgeAmountWei, spaceJediService.getDecimals());
        pledgeEventBean.setPledgeAmount(pledgeAmount);
        pledgeEventBean.setUserAddress((String) event.getArgs().get("user"));
        BigInteger level = (BigInteger) event.getArgs().get("level");
        PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", level.intValue());
        pledgeEventBean.setRegion(pledgeRegion);
        return pledgeEventBean;
    }

    @Override
    public WithdrawEventBean parsedWithdraw(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.WITHDRAW_EVENT);
        if (event == null) {
            return null;
        }
        WithdrawEventBean withdrawEventBean = new WithdrawEventBean();
        BigInteger withdrawAmountWei = (BigInteger) event.getArgs().get("amount");
        Double withdrawAmount = EthMathUtil.bigIntegerToDouble(withdrawAmountWei, spaceJediService.getDecimals());
        withdrawEventBean.setWithrawAmount(withdrawAmount);
        withdrawEventBean.setUserAddress((String) event.getArgs().get("user"));
        BigInteger level = (BigInteger) event.getArgs().get("level");
        PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", level.intValue());
        withdrawEventBean.setRegion(pledgeRegion);
        return withdrawEventBean;
    }
}
