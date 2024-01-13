package top.funsite.spring.action.shiro.configurers;

import lombok.Getter;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import top.funsite.spring.action.shiro.filter.*;

import javax.servlet.Filter;

/**
 * 定义用到的过滤器名称。
 *
 * @see DefaultFilter
 */
@Getter
public enum NamedFilter {

    anon(AnonymousFilter.class),

    authc(AuthFilter.class),

    perms(PermissionsFilter.class),

    roles(RoleFilter.class),

    remember(RememberedFilter.class),

    jwt(JwtFilter.class);

    private final Class<? extends Filter> filterClass;

    NamedFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }
}
