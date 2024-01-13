package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;
import org.springframework.http.HttpStatus;
import top.funsite.spring.action.shiro.MessageConstant;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 关于需要鉴权操作的过滤器。
 *
 * @see RoleFilter
 * @see PermissionFilter
 */
public class AuthorizeFilter extends PassThruFilter {

    /**
     * 就像 {@link #appliedPaths}，保存指定接口需要的多个角色、权限的判断关系。
     */
    protected Map<String, Logical> definedAuthorizationLogicPaths = new LinkedHashMap<>();

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return responseDenied(request, response, HttpStatus.FORBIDDEN, MessageConstant.PermissionDenied);
    }

    /**
     * 获取进行权限检查的逻辑操作。
     *
     * @param request the incoming ServletRequest
     * @return 在指定了多个权限的情况下进行权限检查的逻辑操作，AND 为默认值。
     */
    protected Logical getLogic(ServletRequest request) {
        for (String path : this.definedAuthorizationLogicPaths.keySet()) {
            if (pathsMatch(path, request)) {
                return this.definedAuthorizationLogicPaths.get(path);
            }
        }
        return Logical.AND;
    }
}
