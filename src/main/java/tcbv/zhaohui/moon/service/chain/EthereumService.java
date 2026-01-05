package tcbv.zhaohui.moon.service.chain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.ContractGasProvider;
import tcbv.zhaohui.moon.beans.BlockInfoBean;
import tcbv.zhaohui.moon.beans.TransactionBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.exceptions.ChainException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public interface EthereumService {
    /**
     * 初始化以太坊
     */
    void init();

    Web3j getWeb3j();

    Credentials getCredentials();

    Credentials getPromoteCredentials();

    Map<String, Credentials> getCredentialsMap();

    ContractGasProvider getContractGasProvider();

    Web3Config getWeb3Config();

    /**
     * 根据区块高度获取区块详情
     * @param height 区块高度
     * @return 区块详情
     */
    BlockInfoBean getBlockInfo(long height) throws IOException;

    /**
     * 获取当前区块高度
     * @return 最新高度
     */
    long lastHeigh();

    /**
     * 获取交易详情
     * @param txHash 交易hash
     * @return 交易详情
     */
    TransactionBean getTransactionInfo(String txHash) throws IOException;

    /**
     * 获取账号eth余额
     * @param address 账户地址
     * @return eth余额
     */
    double getEthBalance(String address);

    /**
     * 获取账号nonce值
     * @param address 账号地址
     * @return nonce值
     */
    long getAccountNonce(String address) throws IOException;

    /**
     * 发送eth
     * @param toAddress 目标地址
     * @param amount 金额
     */

    String sendEth(String toAddress, BigDecimal amount) throws IOException;

    /**
     * 解析异常
     * @param te 异常
     * @return 异常信息
     */
    String parseTransactionException(TransactionException te);

    /**
     * 获取合约方法id
     * @param abiJson 合约abi
     * @return 方法id
     */
    String getMethodId(String abiJson, String methodName) throws ChainException;

    /**
     * 校验交易是否正常
     * @param txHash 交易hash
     * @param contractAddress 合约地址
     * @param methodId 方法id
     * @throws ChainException 异常
     */
    void checkTransaction(String txHash, String contractAddress, String methodId) throws ChainException;
}
