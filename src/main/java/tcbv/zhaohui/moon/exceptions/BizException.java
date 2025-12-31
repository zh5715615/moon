package tcbv.zhaohui.moon.exceptions;

import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: BizException
 * @Description:
 * @date: 2025/12/31 10:09
 */
public class BizException extends RuntimeException{
    public static final int LOGIN_TIMEOUT = 50001;

    public static final int LOGIN_VALID_FAILED = 50002;

    public static final int HASH_ALREADY_HANDLE = 50003;

    public static final int NULL_EXCEPTION = 50004;

    public static final int USER_NOT_MATCH = 50005;

    public static final int REGION_NOT_MATCH= 50006;

    @Getter
    private final int code;

    @Getter
    private final String message;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
