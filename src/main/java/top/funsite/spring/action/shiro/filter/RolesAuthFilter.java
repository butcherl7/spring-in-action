package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * @see RolesAuthorizationFilter
 */
public class RolesAuthFilter extends AuthorizationLogicFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        Set<String> roles = CollectionUtils.asSet((String[]) mappedValue);

        if (roles.isEmpty()) {
            return true;
        }

        Logical logic = getAuthLogic(request);

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

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return permissionDenied(request, response);
    }

    public RolesAuthFilter() {
    }

    public RolesAuthFilter(Map<String, Logical> appliedLogicalPaths) {
        this.appliedLogicalPaths = appliedLogicalPaths;
    }
}
