package tcbv.zhaohui.moon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: PledgeRegion
 * @Description:
 * @date: 2025/12/21 11:07
 */
@AllArgsConstructor
@Getter
public enum PledgeRegion {
    LOW(0, 150.0, 30 * 60, 30 * 24 * 60 * 60),

    MID(1, 450.0, 60 * 60, 60 * 24 * 60 * 60),

    HIGH(2, 1350.0, 90 * 60, 90 * 24 * 60 * 60),

    FINAL(3, 2700.0, 180 * 60, 180 * 24 * 60 * 60);

    private final int level;

    private final double amount;

    private final int periodTest;

    private final int periodProd;
}
