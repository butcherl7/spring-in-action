package top.funsite.spring.action.shiro.configurers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
     * 存储在 redis 中的 session 数据的分隔符，默认为空。
     */
    private String sessionKeySeparator = "";
}
