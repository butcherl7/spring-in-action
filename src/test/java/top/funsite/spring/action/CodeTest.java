package top.funsite.spring.action;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

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

    @Test
    void testResourceBundle() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages");
        String string = resourceBundle.getString("login.account.locked");
        String formatted = MessageFormat.format(string, "10");
        System.out.println(formatted);
    }
}
