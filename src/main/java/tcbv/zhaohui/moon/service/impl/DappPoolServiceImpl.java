package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.CardNFTToken;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
}
