package top.funsite.spring.action.config;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.shiro.RedisSubjectDAO;
import top.funsite.spring.action.shiro.RedisSubjectFactory;
import top.funsite.spring.action.shiro.configurers.FilterChainBuilder;
import top.funsite.spring.action.shiro.filter.AuthFilter;
import top.funsite.spring.action.shiro.filter.JwtFilter;
import top.funsite.spring.action.shiro.filter.PermissionsFilter;
import top.funsite.spring.action.shiro.filter.RoleFilter;
import top.funsite.spring.action.shiro.realm.DatabaseRealm;
import top.funsite.spring.action.shiro.session.RedisSessionManager;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.Map;

import static top.funsite.spring.action.shiro.configurers.NamedFilter.*;

/**
 * Shiro 配置类。
 * <ol>
 *     <li>Shiro 重定向为何默认带上 JSESSIONID？See {@link ShiroHttpServletResponse#encodeRedirectURL(String)}.</li>
 * </ol>
 * <p>使用 Bean 定义 Shiro Filter. See {@link ShiroFilterFactoryBean}.</p>
 * <p>401 unauthenticated</p>
 * <p>403 unauthorized</p>
 */
@Configuration
public class ShiroConfig {

    public static final String KEY_SEPARATOR = "AppSessions:";

    public static final String LOGIN_URL = "/login";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 解决 Shiro 权限注解不生效的问题。
     *
     * @return DefaultAdvisorAutoProxyCreator
     * @see RequiresRoles
     * @see RequiresPermissions
     * @see <a href="https://blog.csdn.net/m0_37890289/article/details/94014359">shiro使用注解鉴权时一直404</a>
     */
    @Bean
    // @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        // proxyCreator.setProxyTargetClass(true);
        proxyCreator.setUsePrefix(true);
        return proxyCreator;
    }

    @Bean
    public Realm realm() {
        return new DatabaseRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager(@Autowired Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(new RedisSessionManager(redisTemplate, KEY_SEPARATOR));
        securityManager.setSubjectFactory(new RedisSubjectFactory());
        securityManager.setSubjectDAO(new RedisSubjectDAO());
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(LOGIN_URL);

        FilterChainBuilder filterChainBuilder = createFilterChainDefinition();
        Map<String, String> filterChainMap = filterChainBuilder.buildChainMap();
        Map<String, Logical> appliedLogicalMap = filterChainBuilder.buildAuthLogic();

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 使用重写过的过滤器代替默认的。
        filters.put(authc.name(), new AuthFilter());
        filters.put(roles.name(), new RoleFilter(appliedLogicalMap));
        filters.put(perms.name(), new PermissionsFilter(appliedLogicalMap));
        filters.put(jwt.name(), new JwtFilter());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    /**
     * <p>配置要拦截接口（规则），例如：</p>
     *
     * <p>
     * {@code put("/access/role", "authc, roles[ROLE_A,ROLE_B]")}<br />
     * {@code put("/access/perm", "authc, perms[doc:read]")}
     * </p>
     * "org.apache.catalina.core.StandardHostValve.java:166 status(request, response);"
     *
     * @see DefaultShiroFilterChainDefinition
     * @see <a href="https://www.jianshu.com/p/0bad726d0454">关于springMvc错误重定向/error</a>
     */
    private static FilterChainBuilder createFilterChainDefinition() {
        return FilterChainBuilder.newBuilder()
                .antMatchers("/login", "/error", "/favicon.ico").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("home1").hasRole("home1")
                .antMatchers("/jwt").jwt()
                .antMatchers("/**").authenticated();
    }
}
