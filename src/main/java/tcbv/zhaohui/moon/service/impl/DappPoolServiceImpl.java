package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.events.*;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import tcbv.zhaohui.moon.service.IEthereumService;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.*;

import java.math.BigInteger;

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
    @Qualifier("usdtService")
    private Token20Service usdtService;

    @Autowired
    private ICardNFTTokenService cardNFTTokenService;

    @Override
    public void init(IEthereumService ethereumService, String contractAddress) {
        super.init(ethereumService);
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
    public NFTTradeOrderEventBean parseTradeOrder(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.TRADEORDER_EVENT);
        if (event == null) {
            return null;
        }
        NFTTradeOrderEventBean nftTradeOrderEventBean = new NFTTradeOrderEventBean();
        nftTradeOrderEventBean.setTokenId(((BigInteger) event.getArgs().get("tokenId")).toString(10));
        nftTradeOrderEventBean.setSellerAddress((String) event.getArgs().get("seller"));
        nftTradeOrderEventBean.setBuyerAddress((String) event.getArgs().get("buyer"));
        nftTradeOrderEventBean.setPrice(EthMathUtil.bigIntegerToDouble((BigInteger) event.getArgs().get("price"), spaceJediService.getDecimals()));
        return nftTradeOrderEventBean;
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

    @Override
    public BuySpaceJediPackageEventBean parseBuySpaceJediPackage(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.BUYSPACEJEDIPACKAGE_EVENT);
        if (event == null) {
            return null;
        }
        BuySpaceJediPackageEventBean buySpaceJediPackageEventBean = new BuySpaceJediPackageEventBean();
        buySpaceJediPackageEventBean.setBuyerAddress((String) event.getArgs().get("buyer"));
        BigInteger totalCostWei = (BigInteger) event.getArgs().get("totalCost");
        Double totalCost = EthMathUtil.bigIntegerToDouble(totalCostWei, spaceJediService.getDecimals());
        buySpaceJediPackageEventBean.setTotalCost(totalCost);
        BigInteger priceWei = (BigInteger) event.getArgs().get("price");
        Double price = EthMathUtil.bigIntegerToDouble(priceWei, usdtService.getDecimals());
        buySpaceJediPackageEventBean.setPrice(price);
        buySpaceJediPackageEventBean.setStage(((BigInteger) event.getArgs().get("stage")).intValue());
        buySpaceJediPackageEventBean.setBuyCnt(((BigInteger) event.getArgs().get("buyCnt")).intValue());
        return buySpaceJediPackageEventBean;
    }


    @Override
    public SubmitOrderEventBean parseSubmitOrder(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.SUBMITORDER_EVENT);
        if (event == null) {
            return null;
        }
        SubmitOrderEventBean submitOrderEventBean = new SubmitOrderEventBean();
        submitOrderEventBean.setOwner((String) event.getArgs().get("owner"));
        submitOrderEventBean.setTokenId(((BigInteger) event.getArgs().get("tokenId")).toString(10));
        submitOrderEventBean.setPrice(EthMathUtil.bigIntegerToDouble((BigInteger) event.getArgs().get("price"), spaceJediService.getDecimals()));
        return submitOrderEventBean;
    }

    @Override
    public CancelOrderEventBean parseCancelOrder(String txHash) throws Exception {
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.CANCELORDER_EVENT);
        if (event == null) {
            return null;
        }
        SubmitOrderEventBean cancelOrderEventBean = new SubmitOrderEventBean();
        cancelOrderEventBean.setOwner((String) event.getArgs().get("owner"));
        cancelOrderEventBean.setTokenId(((BigInteger) event.getArgs().get("tokenId")).toString(10));
        return cancelOrderEventBean;
    }
}
