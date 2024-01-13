package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 要求请求用户通过身份验证才能继续请求。
 *
 * @see FormAuthenticationFilter
 * @see Subject#isAuthenticated()
 */
public class AuthFilter extends PassThruFilter {
}
