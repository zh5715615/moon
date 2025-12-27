package tcbv.zhaohui.moon.beans.events;

import lombok.Data;

/**
 * @author: zhaohui
 * @Title: CancelOrderEventBean
 * @Description:
 * @date: 2025/12/27 19:12
 */
@Data
public class CancelOrderEventBean {
    private String owner;

    private String tokenId;
}
