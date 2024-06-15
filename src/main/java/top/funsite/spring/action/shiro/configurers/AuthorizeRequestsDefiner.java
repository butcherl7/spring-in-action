package top.funsite.spring.action.shiro.configurers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.AntPathMatcher;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.jetbrains.annotations.ApiStatus;
import top.funsite.spring.action.shiro.filter.AuthorizeFilter;
import top.funsite.spring.action.shiro.filter.RememberedFilter;
import top.funsite.spring.action.shiro.filter.jwt.DecodedJWTValidator;
import top.funsite.spring.action.shiro.filter.jwt.JwtFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于 Shiro 添加基于 URL 的授权。e.g.
 * <pre>
 * {@code
 *  return new AuthorizeRequestsDefiner()
 *      .antMatchers("/login").permitAll()
 *      .antMatchers("/home","/favicon.ico").permitAll()
 *      .antMatchers("/info").hasRole("admin")
 *      .antMatchers("/**").authenticated()
 *      .getDefinedPath();}
 * </pre>
 *
 * @see UsedFilter
 * @see AntPathMatcher
 * @see FilterChainManager#createChain(String, String)
 */
public class AuthorizeRequestsDefiner {

    private final List<RequestMatcherRegistry> requestMatcherRegistries = new ArrayList<>();

    public RequestMatcherRegistry antMatchers(String... antPatterns) {
        RequestMatcherRegistry requestMatcherRegistry = new RequestMatcherRegistry(this, antPatterns);
        requestMatcherRegistries.add(requestMatcherRegistry);
        return requestMatcherRegistry;
    }

    /**
     * 获取接口对应的过滤器链映射，用于创建 Shiro 过滤器截获的过滤器链。
     *
     * @return map.
     * @see ShiroFilterFactoryBean#setFilterChainDefinitionMap(Map)
     */
    public Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry registry : requestMatcherRegistries) {
            String chainDefinition = registry.filter.name();
            String[] authorities = registry.authorities;

            if (ArrayUtils.isNotEmpty(authorities)) {
                chainDefinition = UsedFilter.authc.name() + "," + registry.filter.name() + Arrays
                        .stream(authorities)
                        .collect(Collectors.joining(",", "[", "]"));
            }

            for (String antPattern : registry.antPatterns) {
                map.put(antPattern, chainDefinition);
            }
        }
        return map;
    }

    /**
     * 获取接口对应的权限判断逻辑。
     *
     * @return map.
     * @see AuthorizeFilter
     */
    public Map<String, Logical> getLogicDefinitionMap() {
        Map<String, Logical> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry registry : requestMatcherRegistries) {
            if (ArrayUtils.isNotEmpty(registry.authorities)) {
                for (String antPattern : registry.antPatterns) {
                    map.put(antPattern, registry.logical);
                }
            }
        }
        return map;
    }

    /**
     * 获取定义的已解码的 JWT 验证逻辑。
     *
     * @return map.
     * @see JwtFilter
     */
    @ApiStatus.Experimental
    public Map<String, DecodedJWTValidator> getDefinedDecodedJWTValidator() {
        Map<String, DecodedJWTValidator> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry registry : requestMatcherRegistries) {
            DecodedJWTValidator validator = registry.decodedJWTValidator;
            if (validator != null) {
                for (String antPattern : registry.antPatterns) {
                    map.put(antPattern, validator);
                }
            }
        }
        return map;
    }

    public static class RequestMatcherRegistry {

        private final AuthorizeRequestsDefiner authorizeRequestsDefiner;

        private final String[] antPatterns;

        /**
         * 过滤器名称。
         */
        private UsedFilter filter;

        /**
         * 权限（角色）数组。
         */
        private String[] authorities;

        /**
         * 授权判断逻辑。
         */
        private Logical logical = Logical.AND;

        private DecodedJWTValidator decodedJWTValidator;

        /**
         * 通过 JWT 的验证才能访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner jwt() {
            return jwt(null);
        }

        /**
         * 通过 JWT 的验证才能访问 URL.
         *
         * @param decodedJWTValidator 对已经正确解码过的 JWT 进行进一步验证的回调函数。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner jwt(DecodedJWTValidator decodedJWTValidator) {
            this.filter = UsedFilter.jwt;
            this.decodedJWTValidator = decodedJWTValidator;
            return authorizeRequestsDefiner;
        }

        /**
         * 会话已超时，但通过 {@code rememberMe} 保留了主体信息时也可以访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         * @see RememberedFilter
         */
        public AuthorizeRequestsDefiner rememberMe() {
            this.filter = UsedFilter.remember;
            return authorizeRequestsDefiner;
        }

        /**
         * 经过身份验证的用户才能访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner authenticated() {
            this.filter = UsedFilter.authc;
            return authorizeRequestsDefiner;
        }

        /**
         * 拥有指定的角色才能访问 URL.
         *
         * @param role 角色名称。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasRole(String role) {
            return hasAllRoles(role);
        }

        /**
         * 拥有指定的任意一个角色就能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAnyRoles(String... roles) {
            return requiredAuthorities(UsedFilter.roles, Logical.OR, roles);
        }

        /**
         * 拥有指定的所有角色才能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAllRoles(String... roles) {
            return requiredAuthorities(UsedFilter.roles, Logical.AND, roles);
        }

        /**
         * 拥有指定的权限才能访问 URL.
         *
         * @param permission 权限名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasPermission(String permission) {
            return hasAllPermissions(permission);
        }

        /**
         * 拥有指定的任意一个权限就能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAnyPermissions(String... permissions) {
            return requiredAuthorities(UsedFilter.perms, Logical.OR, permissions);
        }

        /**
         * 拥有指定的所有权限才能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAllPermissions(String... permissions) {
            return requiredAuthorities(UsedFilter.perms, Logical.AND, permissions);
        }

        /**
         * 任何人都允许访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner permitAll() {
            this.filter = UsedFilter.anon;
            return authorizeRequestsDefiner;
        }

        private AuthorizeRequestsDefiner requiredAuthorities(UsedFilter filter, Logical logical, String... authorities) {
            this.filter = filter;
            this.logical = logical;
            this.authorities = authorities;
            return authorizeRequestsDefiner;
        }

        RequestMatcherRegistry(AuthorizeRequestsDefiner authorizeRequestsDefiner, String[] antPatterns) {
            this.authorizeRequestsDefiner = authorizeRequestsDefiner;
            this.antPatterns = antPatterns;
        }
    }
}
