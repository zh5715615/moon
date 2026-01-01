package tcbv.zhaohui.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: PresaleStage
 * @Description:
 * @date: 2025/12/31 20:14
 */
@Getter
@AllArgsConstructor
public enum PresaleStage {
    PRESALE_ROUND_1(1, 500, BigDecimal.valueOf(0.01)),
    PRESALE_ROUND_2(2, 400, BigDecimal.valueOf(0.015)),
    PRESALE_ROUND_3(3, 400, BigDecimal.valueOf(0.02)),
    PRESALE_ROUND_4(4, 350, BigDecimal.valueOf(0.025)),
    PRESALE_ROUND_5(5, 350, BigDecimal.valueOf(0.03));

    private final int stage;

    private final int number;

    private final BigDecimal price;
}
