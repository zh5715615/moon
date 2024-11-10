package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.beans.RecordBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface IMoonBaseService {
    /**
     * 初始化
     */
    void init(String contractAddress);

    /**
     * 根据记录id获取记录
     *
     * @param recordId 记录id
     * @return 记录
     */
    RecordBean getRecord(BigInteger recordId) throws Exception;

    /**
     * 分配奖励
     *
     * @param userList 用户列表
     * @param amountList 奖金金额列表
     * @return txHash
     */
    String allocReward(List<String> userList, List<BigInteger> amountList) throws Exception;
}
