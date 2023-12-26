package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * @see PermissionsAuthorizationFilter
 */
public class PermissionsAuthFilter extends AuthorizationLogicFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        if (perms == null || perms.length == 0) {
            return true;
        }

        Logical logic = getAuthLogic(request);

        if (logic == Logical.AND) {
            return subject.isPermittedAll(perms);
        }

        if (logic == Logical.OR) {
            for (String perm : perms) {
                if (subject.isPermitted(perm)) {
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

    public PermissionsAuthFilter() {
    }

    public PermissionsAuthFilter(Map<String, Logical> appliedLogicalPaths) {
        this.appliedLogicalPaths = appliedLogicalPaths;
    }
}
