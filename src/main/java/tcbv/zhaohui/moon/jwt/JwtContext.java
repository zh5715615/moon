package tcbv.zhaohui.moon.jwt;

/**
 * @author: zhaohui
 * @Title: JwtContext
 * @Description:
 * @date: 2025/12/21 9:43
 */
public class JwtContext {
    private static final ThreadLocal<String> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ADDRESS = new ThreadLocal<>();

    private JwtContext() {}

    public static void setUserId(String userId) {
        USER_ID.set(userId);
    }

    public static String getUserId() {
        return USER_ID.get();
    }

    public static void setAddress(String address) {
        ADDRESS.set(address);
    }

    public static String getAddress() {
        return ADDRESS.get();
    }

    public static void clear() {
        USER_ID.remove();
    }
}
