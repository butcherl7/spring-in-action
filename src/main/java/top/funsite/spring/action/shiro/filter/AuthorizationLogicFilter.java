package top.funsite.spring.action.shiro.filter;

import org.apache.shiro.authz.annotation.Logical;

import javax.servlet.ServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 增加授权逻辑操作的功能。
 */
public class AuthorizationLogicFilter extends PassThruFilter {

    /**
     * 就像 {@link #appliedPaths}，保存指定接口需要的多个角色、权限的判断关系。
     */
    protected Map<String, Logical> appliedLogicalPaths = new LinkedHashMap<>();

    /**
     * 获取进行权限检查的逻辑操作。
     *
     * @param request the incoming ServletRequest
     * @return 在指定了多个权限的情况下进行权限检查的逻辑操作，AND 为默认值。
     */
    protected Logical getAuthLogic(ServletRequest request) {
        for (String path : this.appliedPaths.keySet()) {
            if (pathsMatch(path, request)) {
                return this.appliedLogicalPaths.get(path);
            }
        }
        return Logical.AND;
    }
}
