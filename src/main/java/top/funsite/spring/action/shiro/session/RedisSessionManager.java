package top.funsite.spring.action.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RedisSessionManager extends DefaultWebSessionManager {

    private static final String DEFAULT_SESSION_PARAMETER_NAME = "jsessionid";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionManager(RedisTemplate<String, Object> redisTemplate) {
        setSessionIdCookieEnabled(false);
        setSessionIdUrlRewritingEnabled(false);
        // this.sessionDAO = sessionDAO;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Session getSession(SessionKey key) throws SessionException {
        String jsessionid = getJsessionid(WebUtils.getHttpRequest(key));

        log.debug("jsessionid is {}.", jsessionid);

        if (StringUtils.isBlank(jsessionid) || BlankSession.ID.equals(jsessionid) || Boolean.FALSE.equals(redisTemplate.hasKey(jsessionid))) {
            log.debug("Session is Blank.");
            return BlankSession.getInstance();
        }
        return new RedisSession(redisTemplate, jsessionid);
    }

    private String getJsessionid(HttpServletRequest request) {
        // 防止重定向带上 url 上带 jsessionid.
        // 参见 org.apache.shiro.web.servlet.ShiroHttpServletResponse#isEncodeable
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());

        String jsessionid = request.getHeader(DEFAULT_SESSION_PARAMETER_NAME);
        if (jsessionid == null) {
            jsessionid = request.getParameter(DEFAULT_SESSION_PARAMETER_NAME);
        }
        return jsessionid;
    }
}
