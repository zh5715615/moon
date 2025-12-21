package tcbv.zhaohui.moon.jwt;

import java.lang.annotation.*;

/**
 * @author: zhaohui
 * @Title: JwtAddressRequired
 * @Description:
 * @date: 2025/12/21 9:42
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JwtAddressRequired {
    // 你 JWT 里 address 的 claim key
    String userId() default "sub";

    String address() default "address";

    // true：没 token/没 address 就直接 401
    boolean required() default true;
}