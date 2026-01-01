package tcbv.zhaohui.moon.service.chain.impl;

import lombok.Getter;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.Token20Contract;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

import static tcbv.zhaohui.moon.exceptions.ChainException.QUERY_EXCEPTION;

/**
 * @author: zhaohui
 * @Title: Token20ServiceImpl
 * @Description:
 * @date: 2025/12/20 18:14
 */
public class Token20ServiceImpl extends EthereumServiceImpl implements Token20Service {

    private Token20Contract token20Contract;

    @Getter
    private int decimals;

    private String contractAddress;

    @Override
    public void init(EthereumService ethereumService, String contractAddress) {
        super.init(ethereumService);
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        this.token20Contract = Token20Contract.load(contractAddress, web3j, transactionManager, contractGasProvider);
        this.decimals = decimals();
        this.contractAddress = contractAddress;
    }

    @Override
    public int decimals() throws ChainException {
        try {
            return token20Contract.decimals().send().intValue();
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 decimals failed: " + e.getMessage());
        }
    }

    @Override
    public String name() throws ChainException {
        try {
            return token20Contract.name().send();
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 name failed: " + e.getMessage());
        }
    }

    @Override
    public String symbol() throws ChainException {
        try {
            return token20Contract.symbol().send();
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 symbol failed: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal totalSupply() throws ChainException {
        try {
            BigInteger totalSupply = token20Contract.totalSupply().send();
            return EthMathUtil.bigIntegerToBigDecimal(totalSupply, decimals);
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 totalSupply failed: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal allowance(String owner, String spender) throws ChainException {
        try {
            BigInteger allowance = token20Contract.allowance(owner, spender).send();
            return EthMathUtil.bigIntegerToBigDecimal(allowance, decimals);
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 allowance failed: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal balanceOf(String accountAddress) throws ChainException {
        try {
            BigInteger bigInteger = token20Contract.balanceOf(accountAddress).send();
            return EthMathUtil.bigIntegerToBigDecimal(bigInteger, decimals);
        } catch (Exception e) {
            throw new ChainException(QUERY_EXCEPTION, "Query token20 balanceOf failed: " + e.getMessage());
        }
    }

    @Override
    @Web3TxGuard
    public String transfer(String toAddress, BigDecimal value) throws Exception {
        return token20Contract.transfer(toAddress, EthMathUtil.decimalToBigInteger(value, decimals)).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String transfer(String fromAddress, String toAddress, BigDecimal value) throws Exception {
        Token20Contract localToken20Contract;
        if (fromAddress.equalsIgnoreCase(web3Config.getPromoterAddress())) {
            localToken20Contract = Token20Contract.load(contractAddress, web3j, promoteCredentials, contractGasProvider);
        } else {
            localToken20Contract = Token20Contract.load(contractAddress, web3j, mgr500Account.get(fromAddress), contractGasProvider);
        }
        return localToken20Contract.transfer(toAddress, EthMathUtil.decimalToBigInteger(value, decimals)).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String transferFrom(String fromAddress, String toAddress, BigDecimal value) throws Exception {
        return token20Contract.transferFrom(fromAddress, toAddress, EthMathUtil.decimalToBigInteger(value, decimals)).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String approve(String spender, BigDecimal value) throws Exception {
        return token20Contract.approve(spender, EthMathUtil.decimalToBigInteger(value, decimals)).send().getTransactionHash();
    }

    @Override
    public String contractAddress() {
        return contractAddress;
    }
}
