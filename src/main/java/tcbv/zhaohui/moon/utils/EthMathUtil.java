package tcbv.zhaohui.moon.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * (TatgMathUtil)tatg数学转换工具
 *
 * @author zhaohui
 * @since 2023/12/6 17:49
 */
public class EthMathUtil {

    public static long bigIntegerToLong(BigInteger bigInteger) {
        return bigInteger.longValue();
    }

//    /**
//     * 大整数转浮点型
//     * @param bigInteger 大整数
//     * @param decimals 精度
//     * @return 浮点型金额
//     */
//    public static double bigIntegerToDouble(BigInteger bigInteger, int decimals) {
//        BigDecimal bigDecimal = new BigDecimal(bigInteger);
//        return bigDecimal.divide(BigDecimal.TEN.pow(decimals)).doubleValue();
//    }
//
//    /**
//     * 浮点型转大整数
//     * @param d 浮点型
//     * @param decimals 精度
//     * @return 大整数金额
//     */
//    public static BigInteger doubleToBigInteger(double d, int decimals) {
//        BigDecimal bigDecimal = BigDecimal.valueOf(d);
//        return decimalToBigInteger(bigDecimal, decimals);
//    }

    public static BigInteger decimalToBigInteger(BigDecimal bigDecimal, int decimals) {
        return bigDecimal.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger();
    }

    /**
     * 大整数转浮点型
     * @param bigInteger 大整数
     * @param decimals 精度
     * @return 浮点型金额
     */
    public static BigDecimal bigIntegerToBigDecimal(BigInteger bigInteger, int decimals) {
        BigDecimal bigDecimal = new BigDecimal(bigInteger);
        return bigDecimal.movePointLeft(decimals);
    }
}
