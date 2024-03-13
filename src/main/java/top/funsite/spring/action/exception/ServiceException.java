package top.funsite.spring.action.exception;

import lombok.Getter;
import top.funsite.spring.action.domin.ServiceStatus;

import java.io.Serial;
import java.util.Objects;

@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7546486819294850832L;

    protected final ServiceStatus status;

    public ServiceException(ServiceStatus status, String message) {
        super(message);
        Objects.requireNonNull(message, "message must not be null");
        Objects.requireNonNull(status, "status must not be null");
        this.status = status;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

}
