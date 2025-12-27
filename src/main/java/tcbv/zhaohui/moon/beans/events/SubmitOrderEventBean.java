package tcbv.zhaohui.moon.beans.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: zhaohui
 * @Title: SubmitOrderEventBean
 * @Description:
 * @date: 2025/12/27 11:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmitOrderEventBean extends CancelOrderEventBean {
    private Double price;
}
