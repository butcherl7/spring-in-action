package top.funsite.spring.action.shiro.session;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.config.ShiroConfig;
import top.funsite.spring.action.domin.UserDTO;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static top.funsite.spring.action.util.DateUtils.SIMPLE_MILLI_FORMATTER;

@Slf4j
public class RedisSession implements ValidatingSession {

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
        String lastRememberedAccessTime = "lastRememberedAccessTime";
    }

    /**
     * 禁止作为 session key 的键。
     */
    private static final List<String> PROHIBITED_SET_KEYS = List.of(
            // 不需要把 principals 存到 session. 也有序列化异常的问题。
            DefaultSubjectContext.AUTHENTICATED_SESSION_KEY,
            DefaultSubjectContext.PRINCIPALS_SESSION_KEY
    );

    /**
     * 禁止删除的键。
     */
    private static final List<String> PROHIBITED_REMOVED_KEYS = List.of(
            Key.id,
            Key.host,
            Key.user,
            Key.realmName,
            Key.rememberMe,
            Key.startTimestamp,
            Key.lastAccessTime,
            Key.lastRememberedAccessTime
    );

    /**
     * 构造函数。
     *
     * @param redisTemplate RedisTemplate
     * @param sessionKey    session 的 key 值。
     */
    public RedisSession(RedisTemplate<String, Object> redisTemplate, String sessionKey) {
        Objects.requireNonNull(redisTemplate, "redisTemplate must not be null");
        Objects.requireNonNull(sessionKey, "sessionKey must not be null");

        this.sessionKey = sessionKey;
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
        this.session = hashOperations.entries(sessionKey);
    }

    @Override
    public Serializable getId() {
        return cast(session.get(Key.id), Serializable.class);
    }

    @Override
    public Date getStartTimestamp() {
        return cast(session.get(Key.startTimestamp), Date.class);
    }

    @Override
    public Date getLastAccessTime() {
        return cast(session.get(Key.lastAccessTime), Date.class);
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
        return cast(session.get(Key.host), String.class);
    }

    @Override
    public void touch() throws InvalidSessionException {
        // 如果会话超时就更新 lastRememberedAccessTime.
        String timeKey = isTimeout() ? Key.lastRememberedAccessTime : Key.lastAccessTime;
        hashOperations.put(sessionKey, timeKey, new Date());
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
            if (PROHIBITED_SET_KEYS.contains(sKey)) {
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
            if (PROHIBITED_REMOVED_KEYS.contains(sKey)) {
                throw new InvalidSessionException(sKey + " cannot be removed.");
            }
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
     * @return {@code True} 表示会话已超时。
     */
    public boolean isTimeout() {
        // 没有启用 RememberMe 则永远返回 false
        if (!ShiroConfig.getRememberMe().isEnabled()) {
            return false;
        }

        if (timeout != null) {
            return timeout;
        }

        long duration = ShiroConfig.getRememberMe().getTimeout().getSeconds() * 1000L;

        if (duration > 0) {
            Date lastAccessDate = getLastAccessTime();

            long lastAccessTime = lastAccessDate.getTime();
            long currentTime = System.currentTimeMillis();
            long diff = currentTime - lastAccessTime;

            if (log.isDebugEnabled()) {
                log.debug("CurrentTime   : {}", SIMPLE_MILLI_FORMATTER.format(new Date(currentTime)));
                log.debug("LastAccessTime: {}", SIMPLE_MILLI_FORMATTER.format(lastAccessDate));
                log.debug("Diff          : {} ms.", diff);
            }
            timeout = diff > duration;
        } else {
            timeout = false;
        }
        return timeout;
    }

    /**
     * 当前会话的用户，登录时是否选择了 {@code rememberMe}.
     *
     * @return {@code True} 则表示选择了 {@code rememberMe}.
     */
    public boolean isRememberMe() {
        Object object = getAttribute(Key.rememberMe);
        return object instanceof Boolean && (Boolean) object;
    }

    /**
     * 返回 {@code Remembered} 状态下应用程序最后一次接收到来自与此会话关联的用户的请求或方法调用的时间。
     *
     * @return {@code Remembered} 状态下用户最后一次与系统交互的时间（可能为空，如果不在 {@code Remembered} 状态下的话）。
     * @see Subject#isRemembered()
     */
    @Nullable
    public Date getLastRememberedAccessTime() {
        return cast(session.get(Key.lastRememberedAccessTime), Date.class);
    }

    public UserDTO getUser() {
        return cast(session.get(Key.user), UserDTO.class);
    }

    public String getRealmName() {
        return cast(session.get(Key.realmName), String.class);
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
    private static <T> T cast(Object object, Class<T> clazz) {
        return clazz.cast(object);
    }
}
