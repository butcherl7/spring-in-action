package top.funsite.spring.action.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.funsite.spring.action.config.ShiroConfig;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String login(AuthenticationToken authToken) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(authToken);

        Date date = new Date();
        String jsessionid = generateJsessionid();

        String host = authToken instanceof HostAuthenticationToken ? ((HostAuthenticationToken) authToken).getHost() : "";

        Map<String, Object> map = new HashMap<>(16);
        map.put(RedisSession.Key.id, jsessionid);
        map.put(RedisSession.Key.host, host);
        map.put(RedisSession.Key.lastAccessTime, date);
        map.put(RedisSession.Key.startTimestamp, date);
        map.put(RedisSession.Key.user, subject.getPrincipal());
        map.put(RedisSession.Key.realmName, subject.getPrincipals().getRealmNames().iterator().next());
        redisTemplate.opsForHash().putAll(ShiroConfig.getKeySeparator() + jsessionid, map);

        return jsessionid;
    }


    private static String generateJsessionid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
