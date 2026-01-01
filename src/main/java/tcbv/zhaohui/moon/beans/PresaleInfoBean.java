package tcbv.zhaohui.moon.beans;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: PresaleInfoBean
 * @Description:
 * @date: 2025/12/31 19:24
 */
@Data
public class PresaleInfoBean {
    private Integer sold;

    private BigDecimal price;

    private Integer stage;
}
