package top.funsite.spring.action.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class AccountController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("login")
    public String login(@RequestParam String username, @RequestParam String password) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        subject.login(token);

        String jsessionid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

        Date date = new Date();

        Map<String, Object> map = new HashMap<>(16);
        map.put(RedisSession.id, jsessionid);
        map.put(RedisSession.host, "localhost");
        map.put(RedisSession.lastAccessTime, date);
        map.put(RedisSession.startTimestamp, date);
        redisTemplate.opsForHash().putAll(jsessionid, map);

        return "jsessionid: " + jsessionid;
    }

    @GetMapping("info")
    public String info() {
        return "Info";
    }
}
