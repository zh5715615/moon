package tcbv.zhaohui.moon.service.chain;

import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.exceptions.ChainException;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: BullfightingGameSampleService
 * @Description:
 * @date: 2026/1/17 11:14
 */
public interface BullfightingGameSampleService {
    void init(EthereumService ethereumService, String contractAddress);

    String reward(String userAddress, BigDecimal amount) throws Exception;

    BigDecimal getPoolBalance() throws ChainException;

    BuyGameTimesEventBean parseBuyGameTimes(String txHash) throws Exception;
}
