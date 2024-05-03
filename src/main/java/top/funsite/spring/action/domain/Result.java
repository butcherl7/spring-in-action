package top.funsite.spring.action.domain;

import lombok.Value;

@Value
public class Result<T> {

    boolean error;

    String message;

    T data; // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

    long timestamp = System.currentTimeMillis();

    /**
     * 响应业务成功。
     *
     * @param data data.
     * @return OK Result with data.
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(false, "OK", data);
    }

    /**
     * 响应业务失败。
     *
     * @param message msg，错误消息。
     * @return failed Result.
     */
    public static Result<Void> fail(String message) {
        return new Result<>(true, message, null);
    }

    private Result(boolean error, String message, T data) {
        this.data = data;
        this.error = error;
        this.message = message;
    }
}
