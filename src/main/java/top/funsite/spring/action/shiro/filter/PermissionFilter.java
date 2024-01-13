package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Map;

/**
 * 如果当前用户具有指定的权限，则允许访问，如果没有指定的所有权限，则拒绝访问的过滤器。
 *
 * @see PermissionsAuthorizationFilter
 * @see PermissionAnnotationHandler
 */
public class PermissionFilter extends AuthorizeFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        if (perms == null || perms.length == 0) {
            return true;
        }

        Logical logic = getLogic(request);

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

    public PermissionFilter() {
    }

    public PermissionFilter(Map<String, Logical> appliedLogicalPaths) {
        this.definedAuthorizationLogicPaths = appliedLogicalPaths;
    }
}