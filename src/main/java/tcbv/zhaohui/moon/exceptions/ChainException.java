package tcbv.zhaohui.moon.exceptions;

import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: OssException
 * @Description:
 * @date: 2025/12/31 9:22
 */
public class ChainException extends RuntimeException {

    public static final int QUERY_EXCEPTION = 30001;

    public static final int INVOKE_EXCEPTION = 30002;

    @Getter
    private final int code;

    @Getter
    private final String message;

    public ChainException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}