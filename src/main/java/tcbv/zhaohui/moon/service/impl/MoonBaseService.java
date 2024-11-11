package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.RecordBean;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    }

    @Override
    public RecordBean getRecord(BigInteger recordId) throws Exception {
        Tuple6<String, BigInteger, BigInteger, BigInteger, String, String> tuple6 = moonBase.getRecord(recordId).send();
        if (tuple6 == null) {
            return null;
        }
        String token = tuple6.getValue1();
        BigInteger amount = tuple6.getValue2();
        BigInteger fee = tuple6.getValue3();
        BigInteger redeemAmount = tuple6.getValue4();
        String player = tuple6.getValue5();
        String extraData = tuple6.getValue6();
        return new RecordBean(token, amount, fee, redeemAmount, player, extraData);
    }

    @Override
    public String allocReward(List<String> userList, List<BigInteger> amountList) throws Exception {
        byte[] bt = new byte[32];
        return moonBase.distributeAirdrop2(bt, web3Config.getMoonBaseAddress(), amountList, userList).send().getTransactionHash();
    }
}
