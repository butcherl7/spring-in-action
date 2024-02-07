package top.funsite.spring.action.domin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@JsonPropertyOrder({"ok", "msg", "code", "data", "timestamp"})
public class Result<T> {

    private static final ServiceStatus OK = ServiceStatus.OK;
    private static final ServiceStatus ERROR = ServiceStatus.ERROR;

    /**
     * 表示从逻辑上业务处理是否成功。
     */
    private boolean ok;

    /**
     * 状态码。
     */
    private int code;

    /**
     * 提示信息。
     */
    private String msg;

    /**
     * 业务数据。
     */
    private T data; // @JsonInclude(JsonInclude.Include.NON_NULL) // null 不序列化

    /**
     * 响应时间戳。
     */
    private long timestamp;

    /**
     * 响应业务成功。
     *
     * @param data data.
     * @return ok Result with data.
     */
    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<T>();
        result.setOk(true);
        result.setData(data);
        result.setCode(OK.value());
        result.setMsg(OK.reasonPhrase());
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * 响应业务失败，使用 {@link ServiceStatus#reasonPhrase()} 作为错误消息。
     *
     * @param status ServiceStatus.
     * @return fail Result.
     */
    public static Result<Void> fail(ServiceStatus status) {
        return fail(status, status.reasonPhrase());
    }

    /**
     * 响应业务失败。
     *
     * @param status  ServiceStatus.
     * @param message msg，错误消息。
     * @return fail Result.
     */
    public static Result<Void> fail(ServiceStatus status, String message) {
        Objects.requireNonNull(message, "fail message must not be null");

        Result<Void> result = new Result<>();
        result.setOk(false);
        result.setCode(status.value());
        result.setMsg(message);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

}
