package top.funsite.spring.action.config;

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
            switch (e) {
                case UnknownAccountException ignored -> {
                    message = "账号不存在";
                    status = ServiceStatus.UNKNOWN_ACCOUNT;
                }
                case LockedAccountException ignored -> {
                    message = "账号被锁定";
                    status = ServiceStatus.ACCOUNT_LOCKED;
                }
                case DisabledAccountException ignored -> {
                    message = "账号被禁用";
                    status = ServiceStatus.ACCOUNT_DISABLED;
                }
                case IncorrectCredentialsException ignored -> {
                    message = "密码错误";
                    status = ServiceStatus.INCORRECT_CREDENTIALS;
                }
                default -> {
                    log.warn(e.getMessage(), e);
                    message = "身份验证错误";
                }
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
    public Result<Void> handleAuthorizationException(AuthorizationException e, HttpServletResponse response) {
        ServiceStatus status;
        String message;
        if (e instanceof UnauthorizedException) {
            status = ServiceStatus.UNAUTHORIZED;
            message = MessageConstant.PermissionDenied;
        } else {
            status = ServiceStatus.UNAUTHORIZED;
            message = MessageConstant.AccessDenied;
        }
        response.setStatus(status.status());
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
    public Result<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(ServiceStatus.ERROR, "Internal Server Error");
    }
}
