package top.funsite.spring.action.shiro.configurers;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.FilterChainManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用于 Shiro 过滤器截获的过滤器链的构造者类。e.g.
 * <pre>
 * {@code
 *  return FilterChainBuilder.newBuilder()
 *      .antMatchers("/login").permitAll()
 *      .antMatchers("/home","/favicon.ico").permitAll()
 *      .antMatchers("/info").hasRole("admin")
 *      .antMatchers("/**").authenticated()
 *      .build();}
 * </pre>
 *
 * @see DefaultFilter
 * @see FilterChainManager#createChain(String, String)
 */
public class FilterChainBuilder {

    private final List<Auth> auths = new ArrayList<>();

    public Auth antMatchers(String... antPatterns) {
        Auth auth = new Auth(this, antPatterns);
        auths.add(auth);
        return auth;
    }

    private FilterChainBuilder() {
    }

    /**
     * 构建用于 Shiro 过滤器截获的过滤器链。
     *
     * @return FilterChainDefinitionMap.
     */
    public Map<String, String> buildFilterChainMap() {
        Map<String, String> map = new LinkedHashMap<>();
        for (Auth auth : auths) {
            String chainDefinition = auth.filter.name();
            String[] authorities = auth.authorities;

            if (ArrayUtils.isNotEmpty(authorities)) {
                chainDefinition = NamedFilter.authc.name() + "," + auth.filter.name() + Arrays
                        .stream(authorities)
                        .collect(Collectors.joining(",", "[", "]"));
            }

            for (String antPattern : auth.antPatterns) {
                map.put(antPattern, chainDefinition);
            }
        }
        return map;
    }

    public Map<String, Logical> buildAuthorizationLogic() {
        Map<String, Logical> map = new LinkedHashMap<>();
        for (Auth auth : auths) {
            String[] authorities = auth.authorities;
            if (authorities != null && authorities.length > 0) {
                for (String antPattern : auth.antPatterns) {
                    map.put(antPattern, auth.logical);
                }
            }
        }
        return map;
    }


    public static FilterChainBuilder newBuilder() {
        return new FilterChainBuilder();
    }

    public static class Auth {

        private final String[] antPatterns;

        private final FilterChainBuilder filterChainBuilder;

        private NamedFilter filter;

        private String[] authorities;

        private Logical logical = Logical.AND;

        /**
         * 指定通过 JWT 的验证才能允许访问 URL.
         *
         * @return FilterChainBuilder
         */
        public FilterChainBuilder jwt() {
            this.filter = NamedFilter.jwt;
            return filterChainBuilder;
        }

        /**
         * 指定经过身份验证的用户访问 URL.
         *
         * @return FilterChainBuilder
         */
        public FilterChainBuilder authenticated() {
            this.filter = NamedFilter.authc;
            return filterChainBuilder;
        }

        /**
         * 拥有指定的角色才能访问 URL.
         *
         * @param role 角色名称。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasRole(String role) {
            this.filter = NamedFilter.roles;
            this.authorities = new String[]{role};
            return filterChainBuilder;
        }

        /**
         * 拥有指定的任意一个角色才能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasAnyRoles(String... roles) {
            this.filter = NamedFilter.roles;
            this.authorities = roles;
            this.logical = Logical.OR;
            return filterChainBuilder;
        }

        /**
         * 拥有指定的所有角色才能访问 URL.
         *
         * @param roles 角色名称数组。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasAllRoles(String... roles) {
            this.filter = NamedFilter.roles;
            this.authorities = roles;
            return filterChainBuilder;
        }

        /**
         * 拥有指定的所有权限才能访问 URL.
         *
         * @param permission 权限名称数组。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasPermission(String permission) {
            this.filter = NamedFilter.perms;
            this.authorities = new String[]{permission};
            return filterChainBuilder;
        }

        /**
         * 拥有指定的任意一个权限才能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasAnyPermissions(String... permissions) {
            this.filter = NamedFilter.perms;
            this.authorities = permissions;
            this.logical = Logical.OR;
            return filterChainBuilder;
        }

        /**
         * 拥有指定的所有权限才能访问 URL.
         *
         * @param permissions 权限名称数组。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasAllPermissions(String... permissions) {
            this.filter = NamedFilter.perms;
            this.authorities = permissions;
            return filterChainBuilder;
        }

        /**
         * 指定任何人都允许访问 URL.
         *
         * @return FilterChainBuilder
         */
        public FilterChainBuilder permitAll() {
            this.filter = NamedFilter.anon;
            return filterChainBuilder;
        }

        Auth(FilterChainBuilder filterChainBuilder, String[] antPatterns) {
            this.filterChainBuilder = filterChainBuilder;
            this.antPatterns = antPatterns;
        }
    }
}
