package top.funsite.spring.action.shiro.configurers;

import jakarta.servlet.Filter;
import lombok.Getter;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import top.funsite.spring.action.shiro.filter.AuthenticationFilter;
import top.funsite.spring.action.shiro.filter.PermissionFilter;
import top.funsite.spring.action.shiro.filter.RememberedFilter;
import top.funsite.spring.action.shiro.filter.RoleFilter;
import top.funsite.spring.action.shiro.filter.jwt.JwtFilter;

/**
 * 定义用到的过滤器名称。
 *
 * @see DefaultFilter
 */
@Getter
public enum NamedFilter {

    anon(AnonymousFilter.class),

    authc(AuthenticationFilter.class),

    perms(PermissionFilter.class),

    roles(RoleFilter.class),

    remember(RememberedFilter.class),

    jwt(JwtFilter.class);

    private final Class<? extends Filter> filterClass;

    NamedFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }
}
