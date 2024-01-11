package top.funsite.spring.action.shiro.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.authc.PassThruAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import top.funsite.spring.action.config.ShiroConfig;
import top.funsite.spring.action.domin.HttpErrorEntity;
import top.funsite.spring.action.shiro.MessageConstant;
import top.funsite.spring.action.shiro.session.BlankSession;
import top.funsite.spring.action.util.JsonUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 重写关于身份验证和访问被拒绝的行为。
 *
 * @see org.apache.shiro.web.servlet.AbstractFilter
 */
@Slf4j
public class PassThruFilter extends PassThruAuthenticationFilter {

    protected static final SimpleDateFormat MILLI_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * {@inheritDoc}`lgfg
     *
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
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String origin = req.getHeader("Origin");
        resp.setHeader("Access-Control-Allow-Origin", origin);
        resp.setHeader("Access-Control-Allow-Headers", "*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");

        // 放行 OPTIONS 请求
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        Session session = getSubject(request, response).getSession();

        long timeout = ShiroConfig.getTimeout().getSeconds() * 1000L;

        if (session != null && !BlankSession.ID.equals(session.getId()) && timeout > 0) {
            long currentTime = System.currentTimeMillis();
            long lastAccessTime = session.getLastAccessTime().getTime();

            long diff = currentTime - lastAccessTime;

            log.debug("CurrentTime   : {}", MILLI_TIME_FORMAT.format(new Date(currentTime)));
            log.debug("LastAccessTime: {}", MILLI_TIME_FORMAT.format(session.getLastAccessTime()));
            log.debug("Diff          : {} ms", diff);

            if (diff > timeout) {
                log.warn("timeout");
            }
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }


    // org.apache.shiro.web.filter.authz.AuthorizationFilter.onAccessDenied

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return responseDenied(request, response, HttpStatus.UNAUTHORIZED, MessageConstant.AccessDenied);
    }

    protected boolean responseDenied(ServletRequest request, ServletResponse response, HttpStatus status, String message) throws IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setStatus(status.value());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HttpErrorEntity entity = HttpErrorEntity.create(status, message, req.getRequestURI());
        JsonUtils.writeValue(response.getOutputStream(), entity);
        return false;
    }
}
