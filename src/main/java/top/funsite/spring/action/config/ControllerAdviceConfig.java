package top.funsite.spring.action.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.funsite.spring.action.domin.Result;
import top.funsite.spring.action.domin.ServiceStatus;
import top.funsite.spring.action.exception.ServiceException;
import top.funsite.spring.action.shiro.MessageConstant;

/**
 * 匹配全局异常并给出响应。
 *
 * @author kinglyq
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdviceConfig {

    /**
     * 登录异常。
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        String message = e.getMessage();
        ServiceStatus status = ServiceStatus.AUTHENTICATION_ERROR;
        if (message == null) {
            if (e instanceof UnknownAccountException) {
                message = "账号不存在";
                status = ServiceStatus.UNKNOWN_ACCOUNT;
            } else if (e instanceof LockedAccountException) {
                message = "账号被锁定";
                status = ServiceStatus.ACCOUNT_LOCKED;
            } else if (e instanceof DisabledAccountException) {
                message = "账号被禁用";
                status = ServiceStatus.ACCOUNT_DISABLED;
            } else if (e instanceof IncorrectCredentialsException) {
                message = "密码错误";
                status = ServiceStatus.INCORRECT_CREDENTIALS;
            } else {
                log.warn(e.getMessage(), e);
                message = "身份验证错误";
            }
        }
        return Result.fail(status, message);
    }

    /**
     * 鉴权异常。
     *
     * @see UnauthorizedException
     * @see UnauthenticatedException
     */
    @ExceptionHandler(AuthorizationException.class)
    public Result<Void> handleAuthorizationException(AuthorizationException e, HttpServletRequest request, HttpServletResponse response) {
        ServiceStatus status;
        String message;
        if (e instanceof UnauthorizedException) {
            status = ServiceStatus.FORBIDDEN;
            message = MessageConstant.PermissionDenied;
        } else {
            status = ServiceStatus.UNAUTHORIZED;
            message = MessageConstant.AccessDenied;
        }
        response.setStatus(status.value());
        return Result.fail(status, message);
    }

    /**
     * ServiceException.
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleException(ServiceException e) {
        return Result.fail(e.getStatus(), e.getMessage());
    }

    /**
     * 最后捕获所有未知的异常。
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        ServiceStatus status = ServiceStatus.ERROR;
        response.setStatus(status.value());
        return Result.fail(status);
    }
}
