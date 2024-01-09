package top.funsite.spring.action.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.funsite.spring.action.domin.Result;
import top.funsite.spring.action.exception.ServiceException;
import top.funsite.spring.action.shiro.HttpErrorEntity;
import top.funsite.spring.action.shiro.MessageConstant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        if (message == null) {
            if (e instanceof UnknownAccountException) {
                message = "账号不存在";
            } else if (e instanceof DisabledAccountException) {
                message = "账号被禁用";
            } else if (e instanceof IncorrectCredentialsException) {
                message = "密码错误";
            } else {
                log.warn(e.getMessage(), e);
                message = "身份验证错误";
            }
        }
        return Result.fail(message);
    }

    /**
     * 鉴权异常。
     *
     * @see UnauthorizedException
     * @see UnauthenticatedException
     */
    @ExceptionHandler(AuthorizationException.class)
    public HttpErrorEntity handleAuthorizationException(AuthorizationException e, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status;
        String message;
        if (e instanceof UnauthorizedException) {
            status = HttpStatus.FORBIDDEN;
            message = MessageConstant.PermissionDenied;
        } else {
            status = HttpStatus.UNAUTHORIZED;
            message = MessageConstant.AccessDenied;
        }
        response.setStatus(status.value());
        return HttpErrorEntity.create(status, message, request.getRequestURI());
    }

    /**
     * ServiceException.
     */
    @ExceptionHandler(ServiceException.class)
    public Result<Void> handleException(ServiceException e) {
        return Result.fail(e.getMessage());
    }

    /**
     * 最后捕获所有未知的异常。
     */
    @ExceptionHandler(Exception.class)
    public HttpErrorEntity handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(status.value());
        return HttpErrorEntity.create(status, e.getMessage(), request.getRequestURI());
    }
}
