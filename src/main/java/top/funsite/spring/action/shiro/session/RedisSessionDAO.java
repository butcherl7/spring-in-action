package top.funsite.spring.action.shiro.session;

import lombok.Getter;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

@Getter
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisSessionDAO(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return new RedisSession(redisTemplate, (String) sessionId);
    }
}
