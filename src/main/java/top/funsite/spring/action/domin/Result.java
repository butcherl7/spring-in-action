package top.funsite.spring.action.domin;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Result<T> {

    public static final int OK = 0;

    public static final int FAIL = 1;

    private int code;

    private String msg;

    private T data;

    public static Result<Void> fail(String message) {
        Objects.requireNonNull(message, "message must not be null");
        Result<Void> result = new Result<>();
        result.setCode(FAIL);
        result.setMsg(message);
        return result;
    }

}
