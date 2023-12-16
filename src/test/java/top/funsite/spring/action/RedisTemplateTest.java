package top.funsite.spring.action;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.domain.Dolly;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
public class RedisTemplateTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void save() {
        Dolly dolly = new Dolly();
        dolly.setName("Dolly");
        dolly.setSize(25);
        dolly.setGender(false);
        dolly.setDate(new Date());
        dolly.setLocalTime(LocalTime.now());
        dolly.setLocalDate(LocalDate.now());
        dolly.setDateTime(LocalDateTime.now());
        redisTemplate.opsForValue().set("dolly", dolly);
    }

    @Test
    public void get() {
        Object dolly = redisTemplate.opsForValue().get("dolly");
        assertInstanceOf(Dolly.class, dolly);
    }

}
