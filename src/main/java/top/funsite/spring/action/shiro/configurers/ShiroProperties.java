package top.funsite.spring.action.shiro.configurers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Component
@ConfigurationProperties("shiro")
public class ShiroProperties {

    /**
     * 登录接口 URL，默认为 {@code /login}
     */
    @Setter
    private String loginUrl = "/login";

    /**
     * 存储在 redis 中的 session 数据的分隔符，默认为 ""。
     */
    private String sessionKeySeparator = "";

    /**
     * Session 超时时间（默认为 30 分钟），如果未指定持续时间后缀，将使用秒。配置为 0 视为无超时时间。
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration timeout = Duration.ofMinutes(30);

    /**
     * 登录时选择了 {@code rememberMe} 后的 Session 保存时间，默认为 7 天，如果未指定持续时间后缀，将使用天。
     */
    @DurationUnit(ChronoUnit.DAYS)
    private Duration rememberTime = Duration.ofDays(7);

    public void setSessionKeySeparator(String sessionKeySeparator) {
        if (sessionKeySeparator == null) {
            this.sessionKeySeparator = "";
        } else {
            this.sessionKeySeparator = sessionKeySeparator;
        }
    }

    public void setTimeout(Duration timeout) {
        if (timeout == null) {
            this.timeout = Duration.ofSeconds(0);
        } else {
            this.timeout = timeout;
        }
    }

    public void setRememberDays(Duration rememberTime) {
        if (rememberTime == null) {
            this.rememberTime = Duration.ofSeconds(0);
        } else {
            this.rememberTime = rememberTime;
        }
    }
}
