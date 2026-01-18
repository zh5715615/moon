package tcbv.zhaohui.moon.service.chain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.contract.BullfightingGameSample;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.service.chain.BullfightingGameSampleService;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.utils.AbiEventLogDecoder;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import static tcbv.zhaohui.moon.exceptions.ChainException.QUERY_EXCEPTION;

/**
 * @author: zhaohui
 * @Title: BullfightingGameSampleServiceImpl
 * @Description:
 * @date: 2026/1/17 11:16
 */
@Service
public class BullfightingGameSampleServiceImpl extends EthereumServiceImpl implements BullfightingGameSampleService {

    private BullfightingGameSample bullfightingGame;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    private String buyGameTimesMethodId;

    @Override
    public void init(EthereumService ethereumService, String contractAddress) {
        super.init(ethereumService);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        bullfightingGame = BullfightingGameSample.load(contractAddress, web3j, transactionManager, contractGasProvider);
        buyGameTimesMethodId = getMethodId(BullfightingGameSample.ABI_JSON, BullfightingGameSample.FUNC_BUYGAMETIMES);
    }

    @Override
    public String reward(String userAddress, BigDecimal amount) throws Exception {
        BigInteger amountWei = EthMathUtil.decimalToBigInteger(amount, spaceJediService.getDecimals());
        return bullfightingGame.reward(userAddress, amountWei).send().getTransactionHash();
    }

    @Override
    public BigDecimal getPoolBalance() throws ChainException {
        try {
            return new BigDecimal(bullfightingGame.getPoolBalance().send());
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "getPoolBalance error" + e.getMessage());
        }
    }

    @Override
    public BuyGameTimesEventBean parseBuyGameTimes(String txHash) throws Exception {
        checkTransaction(txHash, web3Config.getGameContractAddress(), buyGameTimesMethodId);
        AbiEventLogDecoder.DecodedEvent event = AbiEventLogDecoder.decodeTxEvents(web3j, txHash, BullfightingGameSample.ABI_JSON, BullfightingGameSample.BUYGAMETIMES_EVENT);
        if (event == null) {
            return null;
        }
        BuyGameTimesEventBean buyGameTimesEventBean = new BuyGameTimesEventBean();
        buyGameTimesEventBean.setUserAddress((String) event.getArgs().get("user"));
        buyGameTimesEventBean.setAmount(EthMathUtil.bigIntegerToBigDecimal((BigInteger) event.getArgs().get("amount"), spaceJediService.getDecimals()));
        buyGameTimesEventBean.setTimes(((BigInteger) event.getArgs().get("times")).intValue());
        return buyGameTimesEventBean;
    }
}
