package tcbv.zhaohui.moon.service;

import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.exceptions.TransactionException;
import tcbv.zhaohui.moon.beans.BlockInfoBean;
import tcbv.zhaohui.moon.beans.TransactionBean;

import java.io.IOException;

public interface IEthereumService {
    /**
     * 初始化以太坊
     */
    void init();

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

    String sendEth(String toAddress, double amount) throws IOException;

    /**
     * 解析异常
     * @param te 异常
     * @return 异常信息
     */
    String parseTransactionException(TransactionException te);
}
