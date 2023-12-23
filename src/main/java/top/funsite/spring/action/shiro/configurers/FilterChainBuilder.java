package top.funsite.spring.action.shiro.configurers;

import org.apache.commons.lang3.ArrayUtils;
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
    public Map<String, String> build() {
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

    public static FilterChainBuilder newBuilder() {
        return new FilterChainBuilder();
    }

    public static class Auth {

        private final FilterChainBuilder filterChainBuilder;

        private final String[] antPatterns;

        private NamedFilter filter;

        private String[] authorities;

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
         * 指定需要某些角色才能访问 URL.
         *
         * @param roles 定义的任意角色字符串。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasRole(String... roles) {
            this.filter = NamedFilter.roles;
            this.authorities = roles;
            return filterChainBuilder;
        }

        /**
         * 指定需要某些权限才能访问 URL.
         *
         * @param permissions 定义的任意权限字符串。
         * @return FilterChainBuilder
         */
        public FilterChainBuilder hasPermission(String... permissions) {
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
