package top.funsite.spring.action.domin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Value;

@Value
@JsonPropertyOrder({"timestamp", "status", "msg", "data",})
public class Result<T> {

    private static final ServiceStatus OK = ServiceStatus.OK;
    private static final ServiceStatus ERROR = ServiceStatus.ERROR;

    /**
     * 状态码。
     */
    int status;

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
        return new Result<>(OK.status(), null, data);
    }

    /**
     * 响应业务失败。
     *
     * @param message msg，错误消息。
     * @return failed Result.
     */
    public static Result<Void> fail(String message) {
        return Result.fail(ERROR, message);
    }

    /**
     * 响应业务失败。
     *
     * @param status  ServiceStatus.
     * @param message msg，错误消息。
     * @return failed Result.
     */
    public static Result<Void> fail(ServiceStatus status, String message) {
        return new Result<>(status.status(), message, null);
    }

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
}
