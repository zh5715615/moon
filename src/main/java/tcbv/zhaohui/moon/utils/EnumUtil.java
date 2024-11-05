package tcbv.zhaohui.moon.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * (EnumUtils)枚举通用操作
 *
 * @author zhaohui
 * @since 2024/4/24 15:57
 */
public class EnumUtil {
    /**
     * 判断枚举是否包含某个值
     *
     * @param enumClass 枚举类
     * @param value     值
     * @param <T>       枚举类型
     * @return 是否包含
     */
    public static <T extends Enum<T>> boolean contains(Class<T> enumClass, String value) {
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取枚举值
     *
     * @param enumClass 枚举类
     * @param value     值
     * @param <T>       枚举类型
     * @return 枚举值
     */
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, String value) {
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(value)) {
                return enumValue;
            }
        }
        return null;
    }

    /**
     * 获取枚举值
     *
     * @param enumClass 枚举类
     * @param value     值
     * @param <T>       枚举类型
     * @return 枚举值
     */
    public static <T extends Enum<T>> T getEnumValue(Class<T> enumClass, Integer value) {
        for (T enumValue : enumClass.getEnumConstants()) {
            if (enumValue.name().equals(value)) {
                return enumValue;
            }
        }
        return null;
    }

    /**
     * 获取枚举值
     * @param input 输入
     * @return 属性get方法
     */
    private static String convertToMethodName(String input) {
        StringBuilder methodName = new StringBuilder("get");
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    methodName.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    methodName.append(c);
                }
            }
        }

        return methodName.toString();
    }

    /**
     * 获取枚举值
     *
     * @param enumClass   枚举类
     * @param c           类型
     * @param sectionName 配置文件中的section
     * @param <T>         枚举类型
     * @param <V>         类型
     * @return 枚举值
     */
    public static <T extends Enum<T>, V> List<V> getEnumValues(Class<T> enumClass, Class<V> c, String sectionName) {
        List<V> values = new ArrayList<>();
        try {
            T[] enumConstants = enumClass.getEnumConstants();
            for (T enumConstant : enumConstants) {
                enumClass.getDeclaredField(sectionName);
                String methodName = convertToMethodName(sectionName);
                Method method = enumClass.getMethod(methodName);
                String value = (String) method.invoke(enumConstant);
                values.add((V) value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return values;
    }
}
