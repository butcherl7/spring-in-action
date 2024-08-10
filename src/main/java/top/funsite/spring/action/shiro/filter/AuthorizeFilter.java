package top.funsite.spring.action.shiro.filter;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import top.funsite.spring.action.domain.web.Result;
import top.funsite.spring.action.shiro.DeniedMessage;

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
     * 就像 {@link #appliedPaths}，设置接口需要的多个角色或权限的逻辑关系。
     */
    protected Map<String, Logical> logicDefinitionMap = new LinkedHashMap<>();

    @Override
    protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) {
        return responseDenial(response, HttpServletResponse.SC_FORBIDDEN, Result.fail(DeniedMessage.PERMISSION_DENIED));
    }

    /**
     * 获取进行权限检查的逻辑操作。
     *
     * @param request the incoming ServletRequest
     * @return 在指定了多个权限的情况下进行权限检查的逻辑操作，{@code Logical.AND} 为默认值。
     */
    @NotNull
    protected Logical getLogic(ServletRequest request) {
        if (CollectionUtils.isEmpty(this.logicDefinitionMap)) {
            return Logical.AND;
        }

        for (String path : this.logicDefinitionMap.keySet()) {
            if (pathsMatch(path, request)) {
                return this.logicDefinitionMap.get(path);
            }
        }
        return Logical.AND;
    }
}
