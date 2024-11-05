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

    @Override
    public long decimals() throws Exception {
        System.out.println(moonBase.decimals().send().toString(16));
        return moonBase.decimals().send().longValue();
    }

    @Override
    public String tokenName() throws Exception {
        return moonBase.name().send();
    }

    @Override
    public String tokenSymbol() throws Exception {
        return moonBase.symbol().send();
    }

    @Override
    public double queryErc20Balance(String userAddress) throws Exception {
        return EthMathUtil.bigIntegerToDouble(moonBase.balanceOf(userAddress).send(), decimals);
    }

    @Override
    public String approve(String spender, double amount) throws Exception {
        return moonBase.approve(spender, EthMathUtil.doubleToBigInteger(amount, decimals)).send().getTransactionHash();
    }

    @Override
    public String transfer(String toAddress, double amount) throws Exception {
        return moonBase.transfer(toAddress, EthMathUtil.doubleToBigInteger(amount, decimals)).send().getTransactionHash();
    }

    @Override
    public String transferFrom(String fromAddress, String toAddress, double amount) throws Exception {
        return moonBase.transferFrom(fromAddress, toAddress, EthMathUtil.doubleToBigInteger(amount, decimals)).send().getTransactionHash();
    }

    @Override
    public double totalSupply() throws Exception {
        return EthMathUtil.bigIntegerToDouble(moonBase.totalSupply().send(), decimals);
    }

    @Override
    public double allowance(String owner, String spender) throws Exception {
        return EthMathUtil.bigIntegerToDouble(moonBase.allowance(owner, spender).send(), decimals);
    }

    @Override
    public String burn(double amount) throws Exception {
        return moonBase.burn(EthMathUtil.doubleToBigInteger(amount, decimals)).send().getTransactionHash();
    }

    @Override
    public String burnFrom(String account, double amount) throws Exception {
        return moonBase.burnFrom(account, EthMathUtil.doubleToBigInteger(amount, decimals)).send().getTransactionHash();
    }

    @Override
    public BigDecimal currentMode() throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(moonBase.currentMode().send(), decimals);
    }

    @Override
    public String feeAddresses(double param) throws Exception {
        return moonBase.feeAddresses(EthMathUtil.doubleToBigInteger(param, decimals)).send();
    }

    @Override
    public BigDecimal feePercents(BigInteger param0) throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(moonBase.feePercents(param0).send(), decimals);
    }

    @Override
    public BigDecimal feeRate() throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(moonBase.feeRate().send(), decimals);
    }

    @Override
    public String updateWhitelist(String account, Boolean status) throws Exception {
        return moonBase.updateWhitelist(account, status).send().getTransactionHash();
    }

    @Override
    public Boolean whitelist(String account) throws Exception {
        return moonBase.whitelist(account).send();
    }
}
