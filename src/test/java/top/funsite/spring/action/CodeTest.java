package top.funsite.spring.action;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
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

    /**
     * @see <a href="https://baike.baidu.com/item/%E8%AF%AD%E8%A8%80%E4%BB%A3%E7%A0%81/6594123?fr=aladdin">语言代码_百度百科</a>
     */
    @Test
    void testResourceBundle() {
        Locale locale = Locale.of("zh", "hant");

        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/messages", locale);
        String string = resourceBundle.getString("login.account.locked");
        String formatted = MessageFormat.format(string, "10");
        System.out.println(formatted);
    }
}
