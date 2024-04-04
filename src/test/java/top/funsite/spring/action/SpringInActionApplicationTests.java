package top.funsite.spring.action;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.domain.Dolly;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
class SpringInActionApplicationTests {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    public void testRedisSet() {
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
    public void testRedisGet() {
        Object dolly = redisTemplate.opsForValue().get("dolly");
        assertInstanceOf(Dolly.class, dolly);
    }

}
