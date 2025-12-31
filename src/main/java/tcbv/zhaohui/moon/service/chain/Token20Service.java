package tcbv.zhaohui.moon.service.chain;

/**
 * @author: zhaohui
 * @Title: Token20Service
 * @Description:
 * @date: 2025/12/20 18:12
 */
public interface Token20Service {
    /**
     * 初始化
     * @param contractAddress 合约地址
     */
    void init(EthereumService ethereumService, String contractAddress);

    /**
     * 获取token精度
     * @return 精度
     */
    int decimals();

    /**
     * 获取token名称
     * @return token名称
     */
    String name();

    /**
     * 获取token标志
     * @return token标志
     */
    String symbol();

    /**
     * 获取发行量
     * @return 发行量
     */
    double totalSupply();

    /**
     * 获取审批对象的金额
     * @param owner 审批人
     * @param spender 被审批人
     * @return 金额
     */
    double allowance(String owner, String spender);

    /**
     * 用户余额
     * @param accountAddress 用户地址
     * @return 余额
     */
    double balanceOf(String accountAddress);

    /**
     * 转账
     * @param toAddress 转账目的地址
     * @param value 金额
     */
    String transfer(String toAddress, double value) throws Exception;

    String transfer(String fromAddress, String toAddress, double value)  throws Exception;

    /**
     * 代理转账
     * @param fromAddress 转账发起地址
     * @param toAddress 转账目的地址
     * @param value 金额
     */
    String transferFrom(String fromAddress, String toAddress, double value) throws Exception;

    /**
     * 授权审批
     * @param spender 被授权这地址
     * @param value 授权金额
     */
    String approve(String spender, double value) throws Exception;

    /**
     * 获取合约地址
     * @return 合约地址
     */
    String contractAddress();

    /**
     * 获取精度值
     * @return 精度值
     */
    int getDecimals();
}
