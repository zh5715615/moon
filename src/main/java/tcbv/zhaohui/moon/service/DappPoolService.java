package tcbv.zhaohui.moon.service;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolService
 * @Description:
 * @date: 2025/12/20 17:41
 */
public interface DappPoolService {
    void init(String contractAddress);

    String extractSpaceJediOnlyTest(Double amount) throws Exception;

    String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception;

    String cancelOrder(String owner, BigInteger tokenId) throws Exception;

    void parseTradeOrder(String txHash);
}
