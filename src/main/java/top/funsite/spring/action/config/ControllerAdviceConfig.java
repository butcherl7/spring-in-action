package top.funsite.spring.action.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.funsite.spring.action.domin.Result;
import top.funsite.spring.action.shiro.HttpErrorEntity;

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
        return Result.fail(e.getMessage());
    }

    /**
     * 鉴权异常。
     *
     * @see UnauthorizedException
     * @see UnauthenticatedException
     */
    @ExceptionHandler(AuthorizationException.class)
    public HttpErrorEntity handleAuthorizationException(AuthorizationException e, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = (e instanceof UnauthorizedException) ? HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;
        String message = (e instanceof UnauthorizedException) ? "Permission denied" : "Access denied";
        response.setStatus(status.value());
        return HttpErrorEntity.create(status, message, request.getRequestURI());
    }

    /**
     * 最后捕获所有异常。
     */
    @ExceptionHandler(Exception.class)
    public HttpErrorEntity handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        response.setStatus(status.value());
        return HttpErrorEntity.create(status, e.getMessage(), request.getRequestURI());
    }
}
