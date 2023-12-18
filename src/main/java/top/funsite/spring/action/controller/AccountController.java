package top.funsite.spring.action.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.config.ShiroConfig;
import top.funsite.spring.action.shiro.session.RedisSession;
import top.funsite.spring.action.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        String requestIp = WebUtils.getRequestIp(request);

        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(username, password, requestIp));

        Date date = new Date();
        String jsessionid = generateJsessionid();

        Map<String, Object> map = new HashMap<>(16);
        map.put(RedisSession.Key.id, jsessionid);
        map.put(RedisSession.Key.host, requestIp);
        map.put(RedisSession.Key.lastAccessTime, date);
        map.put(RedisSession.Key.startTimestamp, date);
        map.put(RedisSession.Key.user, subject.getPrincipal());
        map.put(RedisSession.Key.realmName, subject.getPrincipals().getRealmNames().iterator().next());
        redisTemplate.opsForHash().putAll(ShiroConfig.KEY_SEPARATOR + jsessionid, map);

        return "jsessionid: " + jsessionid;
    }

    @GetMapping("info")
    public String info() {
        return "Info";
    }

    @GetMapping("home")
    public String home() {
        throw new RuntimeException("Welcome Home");
        // return "Welcome Home";
    }

    private static String generateJsessionid() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
