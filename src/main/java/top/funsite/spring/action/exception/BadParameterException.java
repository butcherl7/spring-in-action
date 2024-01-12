package top.funsite.spring.action.exception;

import top.funsite.spring.action.domin.ServiceStatus;

/**
 * 由于参数错误引发的异常。
 */
public class BadParameterException extends ServiceException {

    private static final long serialVersionUID = 2769843440275170440L;

    public BadParameterException(String message) {
        super(ServiceStatus.BAD_PARAMETER, message);
    }
}
