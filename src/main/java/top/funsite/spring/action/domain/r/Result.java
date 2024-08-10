package top.funsite.spring.action.domain.r;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * {@code Result} 是一个泛型 {@code record} 类，作为响应接口调用的结果。
 *
 * @param error     状态，{@code true} 表示出现错误（不成功）。
 * @param message   提示信息。
 * @param data      返回数据。
 * @param timestamp 响应时间。
 * @param <T>       数据类型。
 */
public record Result<T>(boolean error, String message, T data, long timestamp) {

    // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

    private static final String OK = "OK";

    /**
     * 响应业务成功。
     *
     * @param data 接口返回的数据。
     * @return 返回成功状态的结果。
     */
    public static <T> Result<T> ok(T data) {
        return new Result<>(false, OK, data);
    }

    /**
     * 响应业务成功，数据是分页的。
     *
     * @param data 分页数据。
     * @return 返回成功状态的分页数据结果.
     */
    public static <T> Result<Pagination<T>> ok(Pagination<T> data) {
        return new Result<>(false, OK, data);
    }

    /**
     * 响应业务失败。
     *
     * @param message 业务失败的描述信息（不能为空）。
     * @return 返回失败状态的结果。
     */
    public static Result<Void> fail(@NotNull String message) {
        Objects.requireNonNull(message, "message must not be null");
        return new Result<>(true, message, null);
    }

    private Result(boolean error, String message, T data) {
        this(error, message, data, System.currentTimeMillis());
    }
}
