package top.funsite.spring.action.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import top.funsite.spring.action.domin.HttpErrorEntity;
import top.funsite.spring.action.domin.Result;
import top.funsite.spring.action.shiro.MessageConstant;
import top.funsite.spring.action.shiro.session.RedisSession;
import top.funsite.spring.action.util.JSONUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

import static top.funsite.spring.action.domin.ServiceStatus.LOGIN_TIMEOUT;

/**
 * 重写关于身份验证和访问被拒绝的行为。
 *
 * @see AbstractShiroFilter
 * @see AuthorizationFilter
 */
@Slf4j
public class PassThruFilter extends PassThruAuthenticationFilter {

    /**
     * {@inheritDoc}
     *
     * @apiNote 使用 {@code final} 修饰是为了重写后向子类直接传递 {@code HttpServletRequest} 和 {@code HttpServletResponse}.
     */
    @Override
    protected final boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return isAccessAllowed((HttpServletRequest) request, (HttpServletResponse) response, mappedValue);
    }

    /**
     * {@inheritDoc}
     *
     * @apiNote 使用 {@code final} 修饰是为了重写后向子类直接传递 {@code HttpServletRequest} 和 {@code HttpServletResponse}.
     */
    @Override
    protected final boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return onAccessDenied((HttpServletRequest) request, (HttpServletResponse) response);
    }

    /**
     * <p>1. 放行 OPTIONS 请求。</p>
     * <p>2. 防止出现跨域问题，参考 <a href="https://www.imooc.com/article/7989">CORS 实现跨域时授权问题（401错误）的解决</a></p>
     * <pre>
     *  {@code
     *  String origin = request.getHeader("Origin");
     *  response.setHeader("Access-Control-Allow-Origin", origin);
     *  response.setHeader("Access-Control-Allow-Headers", "*");
     *  response.setHeader("Access-Control-Allow-Credentials", "true");
     *  response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");}
     *  </pre>
     */
    protected boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response, Object mappedValue) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");

        // 放行 OPTIONS 请求
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        Subject subject = getSubject(request, response);
        Session session = subject.getSession();

        if (session instanceof RedisSession) {
            RedisSession redisSession = ((RedisSession) session);

            boolean timeout = redisSession.isTimeout();
            boolean rememberMe = redisSession.isRememberMe();

            if (timeout && !rememberMe) {
                // 登录超时就主动退出登录。
                subject.logout();
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                JSONUtils.writeValue(response, Result.fail(LOGIN_TIMEOUT));
                return false;
            }
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }

    protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return responseDenied(request, response, HttpStatus.UNAUTHORIZED, MessageConstant.AccessDenied);
    }

    protected boolean responseDenied(HttpServletRequest request, HttpServletResponse response, HttpStatus status, String message) {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HttpErrorEntity entity = HttpErrorEntity.create(status, message, request.getRequestURI());
        JSONUtils.writeValue(response, entity);
        return false;
    }
}
