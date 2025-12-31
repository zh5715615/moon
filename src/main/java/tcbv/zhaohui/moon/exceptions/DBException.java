package tcbv.zhaohui.moon.exceptions;

import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: BizException
 * @Description:
 * @date: 2025/12/31 10:09
 */
public class DBException extends RuntimeException{
    public static final int INSERT_FAILED = 60001;

    @Getter
    private final int code;

    @Getter
    private final String message;

    public DBException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
