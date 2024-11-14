package tcbv.zhaohui.moon.service;

import java.math.BigDecimal;

public interface IUSDTLikeInterfaceService {
    /**
     * 初始化
     */
    void init(String contractAddress);

    /**
     * 获取账户代币余额
     * @param userAddress 账户地址
     * @return 余额
     */
    BigDecimal queryErc20Balance(String userAddress) throws Exception;

    /**
     * 批准代币支付金额
     * @param spender 被批准人地址
     * @param amount 批准金额
     * @return hash地址
     */
    String approve(String spender, BigDecimal amount) throws Exception;

    /**
     * 代币转账
     * @param toAddress to地址
     * @param amount 金额
     * @return hash地址
     */
    String transfer(String toAddress, BigDecimal amount) throws Exception;

    /**
     * 代转账
     * @param fromAddress 支付地址
     * @param toAddress to地址
     * @param amount 金额
     * @return hash地址
     */
    String transferFrom(String fromAddress, String toAddress, BigDecimal amount) throws Exception;

    /**
     * 获取总发行量
     * @return
     */
    BigDecimal totalSupply() throws Exception;

    /**
     * 获取授权额度
     * @param owner 拥有者
     * @param spender 被批准人
     * @return 额度
     */
    BigDecimal allowance(String owner, String spender) throws Exception;
}
