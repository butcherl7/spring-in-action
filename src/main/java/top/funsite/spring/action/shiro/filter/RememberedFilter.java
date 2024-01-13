package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import top.funsite.spring.action.shiro.configurers.AuthorizeRequestsDefiner;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 如果主体会话已超时（但仍然存在），但登录时选择了 {@code rememberMe} 的情况下则允许其通过的 {@code Filter}.
 *
 * @see Subject#isRemembered()
 * @see AuthorizeRequestsDefiner.RequestMatcherRegistry#rememberMe()
 */
public class RememberedFilter extends AuthFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        return SecurityUtils.getSubject().isRemembered();
    }
}
