package top.funsite.spring.action.domain;

/**
 * Result 是一个泛型记录类，用于表示某个接口调用的结果。
 *
 * @param error     表示业务是否出错
 * @param message   提示信息
 * @param data      返回的数据
 * @param timestamp 响应的时间
 */
public record Result<T>(boolean error, String message, T data, long timestamp) {

    // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

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
        this(error, message, data, System.currentTimeMillis());
    }
}
