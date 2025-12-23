package tcbv.zhaohui.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: NftOrderStatusEnum
 * @Description:
 * @date: 2025/12/23 20:29
 */
@AllArgsConstructor
@Getter
public enum NftOrderStatusEnum {
    PENDING(1), //挂单中

    CANCEL(2),  //取消

    TRADED(3);  //交易完成
    private final int status;
}
