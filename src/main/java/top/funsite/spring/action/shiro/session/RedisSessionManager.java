package top.funsite.spring.action.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
public class RedisSessionManager extends DefaultWebSessionManager {

    private static final String DEFAULT_SESSION_PARAMETER_NAME = "jsessionid";

    private final RedisTemplate<String, Object> redisTemplate;

    private final String keySeparator;

    /**
     * 构造函数。
     *
     * @param redisTemplate RedisTemplate
     * @param keySeparator  存储在 redis 中的 session 数据的分隔符（既用 ":" 分隔的形式），例如 {@code new RedisSessionManager(redisTemplate, "UserSessions:")}
     * @see <a href="https://redis.io/docs/manual/patterns/indexes/#adding-auxiliary-information-in-the-index">Adding auxiliary information in the index</a>
     */
    public RedisSessionManager(RedisTemplate<String, Object> redisTemplate, String keySeparator) {
        setSessionIdCookieEnabled(false);
        setSessionIdUrlRewritingEnabled(false);
        this.redisTemplate = redisTemplate;
        this.keySeparator = keySeparator;
    }

    /**
     * 构造函数。
     *
     * @param redisTemplate RedisTemplate
     */
    public RedisSessionManager(RedisTemplate<String, Object> redisTemplate) {
        this(redisTemplate, "");
    }

    @Override
    public Session getSession(SessionKey key) throws SessionException {
        String jsessionid = getJsessionid(WebUtils.getHttpRequest(key));
        String sessionKey = keySeparator + jsessionid;

        log.debug("jsessionid is {}.", jsessionid);
        log.debug("sessionKey is {}.", sessionKey);

        if (isBlank(jsessionid) || Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) {
            log.debug("Session does not exist, return BlankSession.");
            return BlankSession.getInstance();
        }
        return new RedisSession(redisTemplate, sessionKey);
    }

    private String getJsessionid(HttpServletRequest request) {
        // 防止重定向后的 url 上带 jsessionid.
        // 参见 org.apache.shiro.web.servlet.ShiroHttpServletResponse#isEncodeable
        request.setAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED, isSessionIdUrlRewritingEnabled());

        String jsessionid = request.getHeader(DEFAULT_SESSION_PARAMETER_NAME);
        if (jsessionid == null) {
            jsessionid = request.getParameter(DEFAULT_SESSION_PARAMETER_NAME);
        }
        return jsessionid;
    }
}
