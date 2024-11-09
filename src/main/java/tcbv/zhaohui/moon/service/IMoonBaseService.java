package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.beans.RecordBean;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface IMoonBaseService {
    /**
     * 初始化
     */
    void init(String contractAddress);

    /**
     * 根据记录id获取记录
     * @param recordId 记录id
     * @return 记录
     */
    RecordBean getRecord(BigInteger recordId) throws Exception;
}
