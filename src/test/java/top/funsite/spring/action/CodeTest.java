package top.funsite.spring.action;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
public class CodeTest {

    @Test
    void testUnit() {
        LocalDateTime now = LocalDateTime.of(2024, 3, 20, 14, 50, 11);
        LocalDateTime unlockedTime = LocalDateTime.of(2024, 3, 20, 14, 50, 10);

        long until = now.until(unlockedTime, ChronoUnit.SECONDS);

        log.info("{} seconds.", until);
    }

    @Test
    void testNumberCast() {
        double d = 2.01;
        System.out.println((long) d);
        System.out.println(Math.ceil(d));
    }
}
