package tcbv.zhaohui.moon.service;

import org.web3j.abi.EventValues;

/**
 * (ISubscribeCallback)订阅回调处理
 *
 * @author zhaohui
 * @since 2024/3/6 17:51
 */
public interface ISubscribeCallback {
    /**
     * 处理事件
     * @param eventValues 事件值
     */
    void handle(EventValues eventValues);
}
