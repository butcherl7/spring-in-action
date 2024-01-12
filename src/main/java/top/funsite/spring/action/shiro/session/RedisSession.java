package top.funsite.spring.action.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.config.ShiroConfig;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisSession implements ValidatingSession {

    protected static final SimpleDateFormat MILLI_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    private final Map<String, Object> session;

    private final String sessionKey;

    private Boolean timeout = null;

    /// keys

    public interface Key {
        String id = "id";
        String host = "host";
        String user = "user";
        String realmName = "realmName";
        String rememberMe = "rememberMe";
        String startTimestamp = "startTimestamp";
        String lastAccessTime = "lastAccessTime";
    }

    /**
     * 被禁止作为 session key 的名单。
     */
    private static final List<String> BLACK_KEYS = Arrays.asList(
            // 不需要把 principals 存到 session. 也有序列化异常的问题。
            DefaultSubjectContext.AUTHENTICATED_SESSION_KEY,
            DefaultSubjectContext.PRINCIPALS_SESSION_KEY
    );

    /**
     * 构造函数。
     *
     * @param redisTemplate RedisTemplate
     * @param sessionKey    session 的 key 值。
     */
    public RedisSession(RedisTemplate<String, Object> redisTemplate, String sessionKey) {
        this.sessionKey = sessionKey;
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        // 不存在就创建。
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(sessionKey))) {
            redisTemplate.opsForHash().put(sessionKey, Key.id, sessionKey);
        }
        this.session = hashOperations.entries(sessionKey);
    }

    @Override
    public Serializable getId() {
        return cast(session.get(Key.id));
    }

    @Override
    public Date getStartTimestamp() {
        return cast(session.get(Key.startTimestamp));
    }

    @Override
    public Date getLastAccessTime() {
        return cast(session.get(Key.lastAccessTime));
    }

    @Override
    public long getTimeout() throws InvalidSessionException {
        Long expire = redisTemplate.getExpire(sessionKey, TimeUnit.MILLISECONDS);
        return expire == null ? -1 : expire;
    }

    @Override
    public void setTimeout(long maxIdleTimeInMillis) throws InvalidSessionException {
        redisTemplate.expire(sessionKey, maxIdleTimeInMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getHost() {
        return cast(session.get(Key.host));
    }

    @Override
    public void touch() throws InvalidSessionException {
        // 如果已经超时就不再更新访问时间。
        if (isTimeout()) {
            return;
        }
        hashOperations.put(sessionKey, Key.lastAccessTime, new Date());
    }

    @Override
    public void stop() throws InvalidSessionException {
        redisTemplate.delete(sessionKey);
    }

    @Override
    public Collection<Object> getAttributeKeys() throws InvalidSessionException {
        return new HashSet<>(hashOperations.keys(sessionKey));
    }

    @Override
    public Object getAttribute(Object key) throws InvalidSessionException {
        try {
            return hashOperations.get(sessionKey, assertString(key));
        } catch (Exception e) {
            throw new InvalidSessionException(e);
        }
    }

    @Override
    public void setAttribute(Object key, Object value) throws InvalidSessionException {
        try {
            String sKey = assertString(key);

            if (BLACK_KEYS.contains(sKey)) {
                return;
            }
            hashOperations.put(sessionKey, sKey, value);
        } catch (Exception e) {
            throw new InvalidSessionException(e);
        }
    }

    @Override
    public Object removeAttribute(Object key) throws InvalidSessionException {
        Object o = null;
        try {
            String sKey = assertString(key);

            boolean hasKey = hashOperations.hasKey(sessionKey, sKey);
            if (hasKey) {
                o = hashOperations.get(sessionKey, sKey);
                hashOperations.delete(sessionKey, sKey);
            }
        } catch (Exception e) {
            throw new InvalidSessionException(e);
        }
        return o;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws InvalidSessionException {
    }

    /**
     * 判断当前会话是否属于超时会话。
     *
     * @return {@code True} 则表示会话已超时。
     */
    public boolean isTimeout() {
        if (timeout != null) {
            return timeout;
        }

        long duration = ShiroConfig.getTimeout().getSeconds() * 1000L;

        if (duration > 0) {
            long currentTime = System.currentTimeMillis();
            long lastAccessTime = getLastAccessTime().getTime();

            long diff = currentTime - lastAccessTime;

            if (log.isDebugEnabled()) {
                log.debug("CurrentTime   : {}", MILLI_TIME_FORMAT.format(new Date(currentTime)));
                log.debug("LastAccessTime: {}", MILLI_TIME_FORMAT.format(getLastAccessTime()));
                log.debug("Diff          : {} ms.", diff);
            }

            timeout = diff > duration;
        } else {
            timeout = false;
        }
        return timeout;
    }

    private static String assertString(Object key) {
        if (!(key instanceof String)) {
            String msg = "RedisSession based implementations of the Shiro Session interface requires attribute keys " +
                    "to be String objects. The RedisSession class does not support anything other than String keys.";
            throw new IllegalArgumentException(msg);
        }
        return (String) key;
    }

    /**
     * 将传入参数强转为对应的接收类型，如果为 null 则返回 null.
     *
     * @param object Object.
     * @param <T>    接收类型。
     * @return 强转为对应的接收类型。
     */
    @SuppressWarnings("unchecked")
    private static <T> T cast(Object object) {
        if (object == null) {
            return null;
        }
        return (T) object;
    }
}
