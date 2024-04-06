package top.funsite.spring.action.service;

import jakarta.annotation.Resource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.funsite.spring.action.config.ShiroConfig;
import top.funsite.spring.action.shiro.session.RedisSession;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String login(AuthenticationToken authToken) {
        Subject subject = SecurityUtils.getSubject();

        // 如果在已登录的情况那就先退出登录。
        if (subject.isAuthenticated() || subject.isRemembered()) {
            subject.logout();
        }

        subject.login(authToken);

        Date date = new Date();
        String jsessionid = generateJsessionid();
        String redisKey = ShiroConfig.getSessionKeySeparator() + jsessionid;

        String host = authToken instanceof HostAuthenticationToken ? ((HostAuthenticationToken) authToken).getHost() : "";
        boolean rememberMe = authToken instanceof RememberMeAuthenticationToken && ((RememberMeAuthenticationToken) authToken).isRememberMe();

        Map<String, Object> map = new HashMap<>(16);
        map.put(RedisSession.Key.id, jsessionid);
        map.put(RedisSession.Key.host, host);
        map.put(RedisSession.Key.user, subject.getPrincipal());
        map.put(RedisSession.Key.startTimestamp, date);
        map.put(RedisSession.Key.lastAccessTime, date);
        map.put(RedisSession.Key.rememberMe, rememberMe);
        map.put(RedisSession.Key.realmName, subject.getPrincipals().getRealmNames().iterator().next());
        redisTemplate.opsForHash().putAll(redisKey, map);

        long timeout = ShiroConfig.getTimeout().getSeconds();

        if (ShiroConfig.getRememberMe().isEnabled()) {
            timeout = Math.max(timeout, ShiroConfig.getRememberMe().getTimeout().getSeconds());
        }
        redisTemplate.expire(redisKey, timeout, TimeUnit.SECONDS);

        return jsessionid;
    }


    private static String generateJsessionid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
