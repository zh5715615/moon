package tcbv.zhaohui.moon.exceptions;

/**
 * @author: zhaohui
 * @Title: Web3TxGuard
 * @Description:
 * @date: 2025/12/20 20:41
 */
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Web3TxGuard {
}
