package top.funsite.spring.action.exception;

import lombok.Getter;

import java.io.Serial;
import java.util.Objects;

@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7546486819294850832L;

    public ServiceException(String message) {
        super(message);
        Objects.requireNonNull(message, "message must not be null");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
