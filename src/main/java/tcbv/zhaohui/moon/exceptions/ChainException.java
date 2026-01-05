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

    public static final int GENERATE_WALLET_FAILED = 30003;

    public static final int TXHASH_NOT_FOUND = 30004;

    public static final int METHOD_NOT_MATCH = 30005;

    public static final int CONTRACT_ADDRESS_NOT_MATCH = 30006;

    public static final int METHOD_NOT_FOUND = 30007;

    public static final int TX_OF_INVOKE_FAILED = 30008;

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