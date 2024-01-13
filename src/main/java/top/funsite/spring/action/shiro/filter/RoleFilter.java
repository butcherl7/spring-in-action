package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * 如果当前用户具有指定的角色，则允许访问，如果没有指定所有角色，则拒绝访问的过滤器。
 *
 * @see RolesAuthorizationFilter
 * @see RoleAnnotationHandler
 */
public class RoleFilter extends AuthorizeFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Set<String> roles = CollectionUtils.asSet((String[]) mappedValue);

        if (roles.isEmpty()) {
            return true;
        }

        Logical logic = getLogic(request);

        if (logic == Logical.AND) {
            return subject.hasAllRoles(roles);
        }

        if (logic == Logical.OR) {
            for (String role : roles) {
                if (subject.hasRole(role)) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    public RoleFilter() {
    }

    public RoleFilter(Map<String, Logical> authorizationLogicPaths) {
        this.authorizationLogicPaths = authorizationLogicPaths;
    }
}
