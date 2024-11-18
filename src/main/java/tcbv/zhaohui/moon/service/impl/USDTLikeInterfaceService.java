package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.contract.USDTLikeInterface;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.service.IUSDTLikeInterfaceService;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;

@Service
public class USDTLikeInterfaceService extends EthereumService implements IUSDTLikeInterfaceService {

    private USDTLikeInterface usdtLikeInterface;

    private USDTLikeInterface weekToken20;

    private USDTLikeInterface monthToken20;

    public static final int DECIMALS = 6;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        usdtLikeInterface = USDTLikeInterface.load(contractAddress, web3j, transactionManager, contractGasProvider);

        TransactionManager weekTransactionManager = new RawTransactionManager(web3j, weekCredentials, web3Config.getChainId());
        weekToken20 = USDTLikeInterface.load(contractAddress, web3j, weekTransactionManager, contractGasProvider);

        TransactionManager monthTransactionManager = new RawTransactionManager(web3j, moonCredentials, web3Config.getChainId());
        monthToken20 = USDTLikeInterface.load(contractAddress, web3j, monthTransactionManager, contractGasProvider);
    }

    @Override
    public BigDecimal queryErc20Balance(String userAddress) throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(usdtLikeInterface.balanceOf(userAddress).send(), DECIMALS);
    }

    @Override
    public String approve(String spender, BigDecimal amount) throws Exception {
        return usdtLikeInterface.approve(spender, EthMathUtil.decimalToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transfer(String toAddress, BigDecimal amount) throws Exception {
        return usdtLikeInterface.transfer(toAddress, EthMathUtil.decimalToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transferWeek(String toAddress, BigDecimal amount) throws Exception {
        return weekToken20.transfer(toAddress, EthMathUtil.decimalToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transferMooth(String toAddress, BigDecimal amount) throws Exception {
        return monthToken20.transfer(toAddress, EthMathUtil.decimalToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transferFrom(String fromAddress, String toAddress, BigDecimal amount) throws Exception {
        return usdtLikeInterface.transferFrom(fromAddress, toAddress, EthMathUtil.decimalToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public BigDecimal totalSupply() throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(usdtLikeInterface.totalSupply().send(), DECIMALS);
    }

    @Override
    public BigDecimal allowance(String owner, String spender) throws Exception {
        return EthMathUtil.bigIntegerToBigDecimal(usdtLikeInterface.allowance(owner, spender).send(), DECIMALS);
    }
}
