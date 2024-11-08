package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class MoonBaseService extends EthereumService implements IMoonBaseService {

    private MoonBase moonBase;

    private int decimals;

    private String symbol;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        moonBase = MoonBase.load(contractAddress, web3j, transactionManager, contractGasProvider);
        //TODO 合约初始化还存在问题，暂时注释掉
//        try {
//            decimals = (int) decimals();
//            symbol = tokenSymbol();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
