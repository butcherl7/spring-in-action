package top.funsite.spring.action.shiro.configurers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import top.funsite.spring.action.shiro.filter.AuthorizeFilter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于 Shiro 添加基于 URL 的授权。e.g.
 * <pre>
 * {@code
 *  return AuthorizeRequestsDefiner.define()
 *      .antMatchers("/login").permitAll()
 *      .antMatchers("/home","/favicon.ico").permitAll()
 *      .antMatchers("/info").hasRole("admin")
 *      .antMatchers("/**").authenticated()
 *      .getDefinedAuthorizationRequest();}
 * </pre>
 *
 * @see DefaultFilter
 * @see FilterChainManager#createChain(String, String)
 */
public class AuthorizeRequestsDefiner {

    private final List<RequestMatcherRegistry> requestMatcherRegistries = new ArrayList<>();

    public RequestMatcherRegistry antMatchers(String... antPatterns) {
        RequestMatcherRegistry requestMatcherRegistry = new RequestMatcherRegistry(this, antPatterns);
        requestMatcherRegistries.add(requestMatcherRegistry);
        return requestMatcherRegistry;
    }

    private AuthorizeRequestsDefiner() {
    }

    /**
     * 获取定义的 URL 拦截配置。
     *
     * @return map.
     * @see ShiroFilterFactoryBean#setFilterChainDefinitionMap(Map)
     */
    public Map<String, String> getDefinedAuthorizationRequest() {
        Map<String, String> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry requestMatcherRegistry : requestMatcherRegistries) {
            String chainDefinition = requestMatcherRegistry.filter.name();
            String[] authorities = requestMatcherRegistry.authorities;

            if (ArrayUtils.isNotEmpty(authorities)) {
                chainDefinition = NamedFilter.authc.name() + "," + requestMatcherRegistry.filter.name() + Arrays
                        .stream(authorities)
                        .collect(Collectors.joining(",", "[", "]"));
            }

            for (String antPattern : requestMatcherRegistry.antPatterns) {
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
    public Map<String, Logical> getDefinedAuthorizationLogic() {
        Map<String, Logical> map = new LinkedHashMap<>();
        for (RequestMatcherRegistry requestMatcherRegistry : requestMatcherRegistries) {
            String[] authorities = requestMatcherRegistry.authorities;
            if (authorities != null && authorities.length > 0) {
                for (String antPattern : requestMatcherRegistry.antPatterns) {
                    map.put(antPattern, requestMatcherRegistry.logical);
                }
            }
        }
        return map;
    }


    public static AuthorizeRequestsDefiner define() {
        return new AuthorizeRequestsDefiner();
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

        /**
         * 通过 JWT 的验证才能访问 URL.
         *
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner jwt() {
            this.filter = NamedFilter.jwt;
            return authorizeRequestsDefiner;
        }

        /**
         * 经过身份验证的用户才能访问 URL.
         *
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner authenticated() {
            this.filter = NamedFilter.authc;
            return authorizeRequestsDefiner;
        }

        /**
         * 拥有指定的角色才能访问 URL.
         *
         * @param role 角色名称。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasRole(String role) {
            return hasAllRoles(role);
        }

        /**
         * 拥有指定的任意一个角色就能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasAnyRoles(String... roles) {
            return requiredAuthorities(NamedFilter.roles, Logical.OR, roles);
        }

        /**
         * 拥有指定的所有角色才能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasAllRoles(String... roles) {
            return requiredAuthorities(NamedFilter.roles, Logical.AND, roles);
        }

        /**
         * 拥有指定的权限才能访问 URL.
         *
         * @param permission 权限名称数组。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasPermission(String permission) {
            return hasAllPermissions(permission);
        }

        /**
         * 拥有指定的任意一个权限就能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasAnyPermissions(String... permissions) {
            return requiredAuthorities(NamedFilter.perms, Logical.OR, permissions);
        }

        /**
         * 拥有指定的所有权限才能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return FilterChainBuilder
         */
        public AuthorizeRequestsDefiner hasAllPermissions(String... permissions) {
            return requiredAuthorities(NamedFilter.perms, Logical.AND, permissions);
        }

        /**
         * 指定任何人都允许访问 URL.
         *
         * @return FilterChainBuilder
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
