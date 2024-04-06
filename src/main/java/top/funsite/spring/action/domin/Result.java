package top.funsite.spring.action.domin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"timestamp", "error", "status", "msg", "data",})
public class Result<T> {

    boolean error;

    /**
     * 提示信息。
     */
    String message;

    /**
     * 业务数据。
     */
    T data; // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

    /**
     * 响应时间戳。
     */
    long timestamp;

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

    public Result(boolean error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
