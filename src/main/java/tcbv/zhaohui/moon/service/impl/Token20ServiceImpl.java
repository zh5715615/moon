package tcbv.zhaohui.moon.service.impl;

import lombok.Getter;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.Token20Contract;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: Token20ServiceImpl
 * @Description:
 * @date: 2025/12/20 18:14
 */
public class Token20ServiceImpl extends EthereumService implements Token20Service {

    private Token20Contract token20Contract;

    @Getter
    private int decimals;

    private String contractAddress;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        this.token20Contract = Token20Contract.load(contractAddress, web3j, transactionManager, contractGasProvider);
        this.decimals = decimals();
        this.contractAddress = contractAddress;
    }

    @Override
    public int decimals() {
        try {
            return token20Contract.decimals().send().intValue();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String name() {
        try {
            return token20Contract.name().send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String symbol() {
        try {
            return token20Contract.symbol().send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double totalSupply() {
        try {
            BigInteger totalSupply = token20Contract.totalSupply().send();
            return EthMathUtil.bigIntegerToDouble(totalSupply, decimals);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double allowance(String owner, String spender) {
        try {
            BigInteger allowance = token20Contract.allowance(owner, spender).send();
            return EthMathUtil.bigIntegerToDouble(allowance, decimals);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double balanceOf(String accountAddress) {
        try {
            BigInteger bigInteger = token20Contract.balanceOf(accountAddress).send();
            return EthMathUtil.bigIntegerToDouble(bigInteger, decimals);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transfer(String toAddress, double value) {
        try {
            token20Contract.transfer(toAddress, EthMathUtil.doubleToBigInteger(value, decimals)).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transferFrom(String fromAddress, String toAddress, double value) {
        try {
            token20Contract.transferFrom(fromAddress, toAddress, EthMathUtil.doubleToBigInteger(value, decimals)).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void approve(String spender, double value) {
        try {
            token20Contract.approve(spender, EthMathUtil.doubleToBigInteger(value, decimals)).send();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String contractAddress() {
        return contractAddress;
    }
}
