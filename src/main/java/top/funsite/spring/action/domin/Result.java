package top.funsite.spring.action.domin;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Result<T> {

    private int code;

    private String msg;

    private T data;

    private final long timestamp = System.currentTimeMillis();

    public static Result<Void> fail(String message) {
        return fail(ServiceStatus.ERROR, message);
    }

    public static Result<Void> fail(ServiceStatus status, String message) {
        assert status.isError();
        Objects.requireNonNull(message, "fail message must not be null");

        Result<Void> result = new Result<>();
        result.setCode(status.value());
        result.setMsg(message);
        return result;
    }

}
