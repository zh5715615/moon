package tcbv.zhaohui.moon.exceptions;

import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: OssException
 * @Description:
 * @date: 2025/12/31 9:22
 */
public class OssException extends RuntimeException {

    public static final int CONFIG_PARAM_ERROR = 20001;

    public static final int CREATE_BUCKET_ERROR = 20002;

    public static final int DOWNLOAD_ERROR = 20003;

    public static final int NULL_EXCEPTION = 20004;

    public static final int FILE_READ_ERROR = 20005;

    @Getter
    private final int code;

    @Getter
    private final String message;

    public OssException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}