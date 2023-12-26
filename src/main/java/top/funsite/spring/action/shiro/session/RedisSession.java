package top.funsite.spring.action.shiro.session;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RedisSession implements ValidatingSession {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    private final Map<String, Object> session;

    private final String sessionKey;

    /// keys

    public interface Key {
        String id = "id";
        String host = "host";
        String user = "user";
        String realmName = "realmName";
        String startTimestamp = "startTimestamp";
        String lastAccessTime = "lastAccessTime";
    }

    /**
     * 被禁止作为 session key 的名单。
     */
    private static final List<String> KEY_BLACKLIST = Arrays.asList(
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
        hashOperations.put(sessionKey, Key.lastAccessTime, new Date());
    }

    @Override
    public void stop() throws InvalidSessionException {
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

            if (KEY_BLACKLIST.contains(sKey)) {
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
