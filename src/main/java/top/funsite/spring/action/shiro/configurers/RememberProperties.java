package top.funsite.spring.action.shiro.configurers;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Getter
public class RememberProperties {

    /**
     * 是否启用 Remember Me 功能。
     */
    private boolean enabled = false;

    /**
     * Remember Me 时间，默认为 0
     */
    private Duration timeout = Duration.ZERO;
}
