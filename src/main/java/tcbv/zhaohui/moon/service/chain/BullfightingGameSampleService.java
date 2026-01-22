package tcbv.zhaohui.moon.service.chain;

import tcbv.zhaohui.moon.beans.events.BuyGameTimesEventBean;
import tcbv.zhaohui.moon.exceptions.ChainException;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zhaohui
 * @Title: BullfightingGameSampleService
 * @Description:
 * @date: 2026/1/17 11:14
 */
public interface BullfightingGameSampleService {
    void init(EthereumService ethereumService, String contractAddress);

    String reward(List<String> userAddresses, List<BigDecimal> amounts) throws Exception;

    BigDecimal getPoolBalance() throws ChainException;

    BuyGameTimesEventBean parseBuyGameTimes(String txHash) throws Exception;
}
