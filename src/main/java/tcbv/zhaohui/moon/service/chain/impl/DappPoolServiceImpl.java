package tcbv.zhaohui.moon.service.chain.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import software.amazon.ion.Decimal;
import tcbv.zhaohui.moon.beans.PresaleInfoBean;
import tcbv.zhaohui.moon.beans.events.*;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.chain.CardNFTTokenService;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.utils.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import static tcbv.zhaohui.moon.exceptions.ChainException.*;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceImpl
 * @Description:
 * @date: 2025/12/20 17:48
 */
@Service
@Slf4j
public class DappPoolServiceImpl extends EthereumServiceImpl implements DappPoolService {
    private DappPool dappPool;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    @Qualifier("usdtService")
    private Token20Service usdtService;

    @Autowired
    private CardNFTTokenService cardNFTTokenService;

    private String buySpaceJediPackageMethodId;

    private String pledgeMethodId;

    private String withdrawMethodId;

    private String submitOrderMethodId;

    private String cancelOrderMethodId;

    private String tradeOrderMethodId;

    @Override
    public void init(EthereumService ethereumService, String contractAddress) {
        super.init(ethereumService);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        dappPool = DappPool.load(contractAddress, web3j, transactionManager, contractGasProvider);
        buySpaceJediPackageMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_BUYSPACEJEDIPACKAGE);
        pledgeMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_PLEDGE);
        withdrawMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_WITHDRAW);
        submitOrderMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_SUBMITORDER);
        cancelOrderMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_CANCELORDER);
        tradeOrderMethodId = getMethodId(DappPool.ABI_JSON, DappPool.FUNC_TRADEORDER);
    }

    @Override
    @Web3TxGuard
    public String extractSpaceJediOnlyTest(BigDecimal amount) throws Exception {
        BigInteger amountWei = EthMathUtil.decimalToBigInteger(amount, spaceJediService.getDecimals());
        return dappPool.extractSpaceJediOnlyTest(amountWei).send().getTransactionHash();
    }

    @Override
    public PresaleInfoBean getPackageCnt() throws ChainException {
        try {
            Tuple3<BigInteger, BigInteger, BigInteger> tuple3 = dappPool.getPackageCnt().send();
            PresaleInfoBean presaleInfoBean = new PresaleInfoBean();
            presaleInfoBean.setSold(tuple3.component1().intValue());
            presaleInfoBean.setPrice(EthMathUtil.bigIntegerToBigDecimal(tuple3.component2(), usdtService.getDecimals()));
            presaleInfoBean.setStage(tuple3.component3().intValue());
            return presaleInfoBean;
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query package cnt failed: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal getCurrentRewardPercent(PledgeRegion region) throws ChainException {
        try {
            Tuple2<BigInteger, BigInteger> tuple2 = dappPool.getCurrentRewardPercent(BigInteger.valueOf(region.getLevel())).send();
            BigInteger currentRewardPercent = tuple2.component2();
            BigDecimal decimal = new BigDecimal(currentRewardPercent);
            return decimal.divide(Decimal.TEN).divide(Decimal.valueOf(100));
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query current reward percent failed: " + e.getMessage());
        }
    }

    @Override
    public NFTTradeOrderEventBean parseTradeOrder(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), tradeOrderMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.TRADEORDER_EVENT);
        if (event == null) {
            return null;
        }
        NFTTradeOrderEventBean nftTradeOrderEventBean = new NFTTradeOrderEventBean();
        nftTradeOrderEventBean.setTokenId(((BigInteger) event.getArgs().get("tokenId")).toString(10));
        nftTradeOrderEventBean.setSellerAddress((String) event.getArgs().get("seller"));
        nftTradeOrderEventBean.setBuyerAddress((String) event.getArgs().get("buyer"));
        nftTradeOrderEventBean.setPrice(EthMathUtil.bigIntegerToBigDecimal((BigInteger) event.getArgs().get("price"), spaceJediService.getDecimals()));
        return nftTradeOrderEventBean;
    }

    @Override
    public PledgeEventBean parsedPledge(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), pledgeMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.PLEDGE_EVENT);
        if (event == null) {
            return null;
        }
        PledgeEventBean pledgeEventBean = new PledgeEventBean();
        BigInteger pledgeAmountWei = (BigInteger) event.getArgs().get("amount");
        BigDecimal pledgeAmount = EthMathUtil.bigIntegerToBigDecimal(pledgeAmountWei, spaceJediService.getDecimals());
        pledgeEventBean.setPledgeAmount(pledgeAmount);
        pledgeEventBean.setUserAddress((String) event.getArgs().get("user"));
        BigInteger level = (BigInteger) event.getArgs().get("level");
        PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", level.intValue());
        pledgeEventBean.setRegion(pledgeRegion);
        return pledgeEventBean;
    }

    @Override
    public WithdrawEventBean parsedWithdraw(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), withdrawMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.WITHDRAW_EVENT);
        if (event == null) {
            return null;
        }
        WithdrawEventBean withdrawEventBean = new WithdrawEventBean();
        BigInteger withdrawAmountWei = (BigInteger) event.getArgs().get("amount");
        BigDecimal withdrawAmount = EthMathUtil.bigIntegerToBigDecimal(withdrawAmountWei, spaceJediService.getDecimals());
        withdrawEventBean.setWithrawAmount(withdrawAmount);
        withdrawEventBean.setUserAddress((String) event.getArgs().get("user"));
        BigInteger level = (BigInteger) event.getArgs().get("level");
        PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", level.intValue());
        withdrawEventBean.setRegion(pledgeRegion);
        return withdrawEventBean;
    }

    @Override
    public BuySpaceJediPackageEventBean parseBuySpaceJediPackage(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), buySpaceJediPackageMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.BUYSPACEJEDIPACKAGE_EVENT);
        if (event == null) {
            return null;
        }
        BuySpaceJediPackageEventBean buySpaceJediPackageEventBean = new BuySpaceJediPackageEventBean();
        buySpaceJediPackageEventBean.setBuyerAddress((String) event.getArgs().get("buyer"));
        BigInteger totalCostWei = (BigInteger) event.getArgs().get("totalCost");
        BigDecimal totalCost = EthMathUtil.bigIntegerToBigDecimal(totalCostWei, spaceJediService.getDecimals());
        buySpaceJediPackageEventBean.setTotalCost(totalCost);
        BigInteger priceWei = (BigInteger) event.getArgs().get("price");
        BigDecimal price = EthMathUtil.bigIntegerToBigDecimal(priceWei, usdtService.getDecimals());
        buySpaceJediPackageEventBean.setPrice(price);
        buySpaceJediPackageEventBean.setStage(((BigInteger) event.getArgs().get("stage")).intValue());
        buySpaceJediPackageEventBean.setBuyCnt(((BigInteger) event.getArgs().get("buyCnt")).intValue());
        return buySpaceJediPackageEventBean;
    }


    @Override
    public SubmitOrderEventBean parseSubmitOrder(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), submitOrderMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, DappPool.ABI_JSON, DappPool.SUBMITORDER_EVENT);
        if (event == null) {
            return null;
        }
        SubmitOrderEventBean submitOrderEventBean = new SubmitOrderEventBean();
        submitOrderEventBean.setOwner((String) event.getArgs().get("owner"));
        submitOrderEventBean.setTokenId(((BigInteger) event.getArgs().get("tokenId")).toString(10));
        submitOrderEventBean.setPrice(EthMathUtil.bigIntegerToBigDecimal((BigInteger) event.getArgs().get("price"), spaceJediService.getDecimals()));
        return submitOrderEventBean;
    }

    @Override
    public CancelOrderEventBean parseCancelOrder(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getDappPoolContractAddress(), cancelOrderMethodId);
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
