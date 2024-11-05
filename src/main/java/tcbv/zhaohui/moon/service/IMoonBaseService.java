package tcbv.zhaohui.moon.service;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface IMoonBaseService {
    /**
     * 初始化
     */
    void init(String contractAddress);

    /**
     * 获取代币精度
     * @return 精度
     */
    long decimals() throws Exception;

    /**
     * 获取代币名称
     * @return 名称
     */
    String tokenName() throws Exception;

    /**
     * 获取代币标识
     * @return 标识
     */
    String tokenSymbol() throws Exception;

    /**
     * 获取账户代币余额
     * @param userAddress 账户地址
     * @return 余额
     */
    double queryErc20Balance(String userAddress) throws Exception;

    /**
     * 批准代币支付金额
     * @param spender 被批准人地址
     * @param amount 批准金额
     * @return hash地址
     */
    String approve(String spender, double amount) throws Exception;

    /**
     * 代币转账
     * @param toAddress to地址
     * @param amount 金额
     * @return hash地址
     */
    String transfer(String toAddress, double amount) throws Exception;

    /**
     * 代转账
     * @param fromAddress 支付地址
     * @param toAddress to地址
     * @param amount 金额
     * @return hash地址
     */
    String transferFrom(String fromAddress, String toAddress, double amount) throws Exception;

    /**
     * 获取总发行量
     * @return
     */
    double totalSupply() throws Exception;

    /**
     * 获取授权额度
     * @param owner 拥有者
     * @param spender 被批准人
     * @return 额度
     */
    double allowance(String owner, String spender) throws Exception;

    /**
     * 销毁代币
     * @param amount 金额
     * @return hash值
     */
    String burn(double amount) throws Exception;

    /**
     * 销毁代币
     * @param account 账号
     * @param amount 金额
     * @return hash值
     */
    String burnFrom(String account, double amount) throws Exception;

    /**
     * 当前模式
     * @return 模式
     */
    BigDecimal currentMode() throws Exception;

    /**
     * 手续费地址
     * @param param 参数
     * @return 地址
     */
    String feeAddresses(double param) throws Exception;

    /**
     * 手续费比例
     * @param param0 参数
     * @return 比例
     */
    BigDecimal feePercents(BigInteger param0) throws Exception;

    /**
     * 手续费比例
     * @return 比例
     */
    BigDecimal feeRate() throws Exception;

    /**
     * 更新白名单
     * @param account 账号地址
     * @param status 状态
     * @return hash值
     */
    String updateWhitelist(String account, Boolean status) throws Exception;

    /**
     * 是否账号白名单
     * @param account 账号地址
     * @return 是否白名单
     */
    Boolean whitelist(String account) throws Exception;
}
