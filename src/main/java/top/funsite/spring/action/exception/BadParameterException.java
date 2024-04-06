package top.funsite.spring.action.exception;

import java.io.Serial;

/**
 * 由于参数错误引发的异常。
 */
public class BadParameterException extends ServiceException {

    @Serial
    private static final long serialVersionUID = 2769843440275170440L;

    public BadParameterException(String message) {
        super(message);
    }
}
