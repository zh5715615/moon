package tcbv.zhaohui.moon.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.CardNFTToken;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceImpl
 * @Description:
 * @date: 2025/12/20 17:48
 */
@Service
public class DappPoolServiceImpl extends EthereumService implements DappPoolService {
    private DappPool dappPool;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service token20Service;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        dappPool = DappPool.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public String extractSpaceJediOnlyTest(Double amount) throws Exception {
        BigInteger amountWei = EthMathUtil.doubleToBigInteger(amount, token20Service.getDecimals());
        return dappPool.extractSpaceJediOnlyTest(amountWei).send().getTransactionHash();
    }

    @Override
    public String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception {
        BigInteger priceWei = EthMathUtil.doubleToBigInteger(price, 2);
        return dappPool.submitOrder(seller, tokenId, priceWei).send().getTransactionHash();
    }

    @Override
    public String cancelOrder(String owner, BigInteger tokenId) throws Exception {
        return dappPool.cancelOrder(owner, tokenId).send().getTransactionHash();
    }
}
