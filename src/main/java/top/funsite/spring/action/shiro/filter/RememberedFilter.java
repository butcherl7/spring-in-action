package top.funsite.spring.action.shiro.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import top.funsite.spring.action.shiro.configurers.AuthorizeRequestsDefiner;

/**
 * 如果主体会话已超时（但仍然存在），但登录时选择了 {@code rememberMe} 的情况下则允许其通过的 {@code Filter}.
 *
 * @see Subject#isRemembered()
 * @see AuthorizeRequestsDefiner.RequestMatcherRegistry#rememberMe()
 */
public class RememberedFilter extends AuthFilter {

    @Override
    protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (SecurityUtils.getSubject().isRemembered()) {
            return true;
        }
        return super.onAccessDenied(request, response);
    }
}