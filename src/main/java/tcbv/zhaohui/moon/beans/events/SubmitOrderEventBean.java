package tcbv.zhaohui.moon.beans.events;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: SubmitOrderEventBean
 * @Description:
 * @date: 2025/12/27 11:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmitOrderEventBean extends CancelOrderEventBean {
    private BigDecimal price;
}
