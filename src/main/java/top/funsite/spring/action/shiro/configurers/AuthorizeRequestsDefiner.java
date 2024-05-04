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
 * @see NamedFilter
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
     * 获取定义的 URL 拦截配置。
     *
     * @return map.
     * @see ShiroFilterFactoryBean#setFilterChainDefinitionMap(Map)
     */
    public Map<String, String> getDefinedPath() {
        Map<String, String> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry registry : requestMatcherRegistries) {
            String chainDefinition = registry.filter.name();
            String[] authorities = registry.authorities;

            if (ArrayUtils.isNotEmpty(authorities)) {
                chainDefinition = NamedFilter.authc.name() + "," + registry.filter.name() + Arrays
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
     * 获取定义的授权逻辑配置。
     *
     * @return map.
     * @see AuthorizeFilter
     */
    public Map<String, Logical> getDefinedLogic() {
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
        private NamedFilter filter;

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
            this.filter = NamedFilter.jwt;
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
            this.filter = NamedFilter.remember;
            return authorizeRequestsDefiner;
        }

        /**
         * 经过身份验证的用户才能访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner authenticated() {
            this.filter = NamedFilter.authc;
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
            return requiredAuthorities(NamedFilter.roles, Logical.OR, roles);
        }

        /**
         * 拥有指定的所有角色才能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAllRoles(String... roles) {
            return requiredAuthorities(NamedFilter.roles, Logical.AND, roles);
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
            return requiredAuthorities(NamedFilter.perms, Logical.OR, permissions);
        }

        /**
         * 拥有指定的所有权限才能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner hasAllPermissions(String... permissions) {
            return requiredAuthorities(NamedFilter.perms, Logical.AND, permissions);
        }

        /**
         * 任何人都允许访问 URL.
         *
         * @return AuthorizeRequestsDefiner
         */
        public AuthorizeRequestsDefiner permitAll() {
            this.filter = NamedFilter.anon;
            return authorizeRequestsDefiner;
        }

        private AuthorizeRequestsDefiner requiredAuthorities(NamedFilter filter, Logical logical, String... authorities) {
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
