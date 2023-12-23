package top.funsite.spring.action.shiro.configurers;

import org.apache.shiro.web.filter.mgt.DefaultFilter;

/**
 * 定义用到的过滤器名称。
 *
 * @see DefaultFilter
 */
public enum NamedFilter {

    anon,

    authc,

    perms,

    roles,

    jwt
}
