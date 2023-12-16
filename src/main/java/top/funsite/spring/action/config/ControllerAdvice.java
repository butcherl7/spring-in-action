package top.funsite.spring.action.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 匹配全局异常并给出响应。
 *
 * @author kinglyq
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 登录异常处理。
     */
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException e, HttpServletResponse response) {
        settingsResponse(response);
        if (e instanceof UnknownAccountException) {
            return "Unknown Account.";
        } else if (e instanceof IncorrectCredentialsException) {
            return "Incorrect Credentials.";
        } else {
            return "Authentication Exception";
        }
    }

    /**
     * 对 HttpServletResponse 进行一些设置。
     *
     * @param response HttpServletResponse
     */
    private static void settingsResponse(HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }

}
