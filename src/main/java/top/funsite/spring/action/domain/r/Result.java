package top.funsite.spring.action.domain.r;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Result 是一个泛型记录类，作为接口调用响应的结果。
 *
 * @param error     {@code True}表示调用出现错误
 * @param message   提示信息
 * @param data      返回的数据
 * @param timestamp 响应的时间
 */
public record Result<T>(boolean error, String message, T data, long timestamp) {

    // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

    private static final String OK = "OK";

    /**
     * 响应业务成功。
     *
     * @param data data.
     * @return OK Result with data.
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(false, OK, data);
    }

    /**
     * 响应业务失败。
     *
     * @param message 业务失败的描述信息（不能为空）。
     * @return Result.
     */
    public static Result<Void> fail(@NotNull String message) {
        Objects.requireNonNull(message, "message must not be null");
        return new Result<>(true, message, null);
    }

    private Result(boolean error, String message, T data) {
        this(error, message, data, System.currentTimeMillis());
    }
}
