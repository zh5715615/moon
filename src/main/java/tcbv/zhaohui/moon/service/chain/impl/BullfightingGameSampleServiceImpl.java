package tcbv.zhaohui.moon.service.chain.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.contract.BullfightingGameSample;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.chain.BullfightingGameSampleService;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.utils.AbiEventLogDecoder;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
    @Web3TxGuard
    public String reward(List<String> userAddresses, List<BigDecimal> amounts) throws Exception {
        if (userAddresses.size() != amounts.size()) {
            throw new ChainException(QUERY_EXCEPTION, "userAddress and amount length not equal");
        }
        List<BigInteger> amountWeis = new ArrayList<>();
        for (int i = 0; i < amounts.size(); i++) {
            amountWeis.add(EthMathUtil.decimalToBigInteger(amounts.get(i), spaceJediService.getDecimals()));
        }
        return bullfightingGame.reward(userAddresses, amountWeis).send().getTransactionHash();
    }

    @Override
    public BigDecimal getPoolBalance() throws ChainException {
        try {
            return EthMathUtil.bigIntegerToBigDecimal(bullfightingGame.getPoolBalance().send(), spaceJediService.getDecimals());
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
