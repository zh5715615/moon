package tcbv.zhaohui.moon.utils;

/**
 * API响应返回的数据结构
 * @author sunxiaoqiang
 * @date 2021/09/01
 *
 */

import java.io.Serializable;

public class Rsp<T> implements Serializable {
    private static final long serialVersionUID = 5359403958464773388L;

    private int code = 0;

    private T data;

    private String message = "操作成功";

    private long timestamp = System.currentTimeMillis();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public static <T> Rsp<T> ok() {
        return restResult(null, null, null);
    }

    public static <T> Rsp<T> ok(String msg) {
        return restResult(null, msg, null);
    }

    public static <T> Rsp<T> okData(T data) {
        return restResult(null, null, data);
    }

    public static <T> Rsp<T> error() {
        return restResult(1, "操作失败", null);
    }

    public static <T> Rsp<T> error(String message) {
        return restResult(500, message, null);
    }

    public static <T> Rsp<T> error(int code, String message) {
        return restResult(code, message, null);
    }

    private static <T> Rsp<T> restResult(Integer code, String msg, T data) {
        Rsp<T> apiResult = new Rsp<>();
        if (code != null) {
            apiResult.setCode(code);
        }

        if (msg != null && msg.length() > 0) {
            apiResult.setMessage(msg);
        }

        apiResult.setData(data);
        return apiResult;
    }
}
