package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 要求请求用户通过身份验证（已登录）才能继续请求。
 *
 * @see FormAuthenticationFilter
 */
public class AuthFilter extends PassThruFilter {
}
