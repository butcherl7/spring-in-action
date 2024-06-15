package top.funsite.spring.action.shiro.configurers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Component
@ConfigurationProperties("shiro")
public class ShiroProperties {

    /**
     * 登录接口 URL，默认为 {@code /login}
     */
    private String loginUrl = "/login";

    /**
     * 存储在 redis 中 session 数据的分隔符，默认为空。
     */
    private String sessionKeySeparator = "";

    /**
     * Session 超时时间（默认为 30 分钟），如果未指定持续时间后缀，将使用秒。配置为 0 视为无超时时间。
     */
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration timeout = Duration.ofMinutes(30);

    @NestedConfigurationProperty
    private RememberProperties remember = new RememberProperties();
}
