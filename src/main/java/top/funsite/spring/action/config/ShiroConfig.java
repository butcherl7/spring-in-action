package top.funsite.spring.action.config;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.service.UserService;
import top.funsite.spring.action.shiro.RedisSubjectDAO;
import top.funsite.spring.action.shiro.RedisSubjectFactory;
import top.funsite.spring.action.shiro.configurers.AuthorizeRequestsDefiner;
import top.funsite.spring.action.shiro.configurers.ShiroProperties;
import top.funsite.spring.action.shiro.filter.AuthFilter;
import top.funsite.spring.action.shiro.filter.PermissionFilter;
import top.funsite.spring.action.shiro.filter.RememberedFilter;
import top.funsite.spring.action.shiro.filter.RoleFilter;
import top.funsite.spring.action.shiro.filter.jwt.JwtFilter;
import top.funsite.spring.action.shiro.realm.DatabaseRealm;
import top.funsite.spring.action.shiro.session.RedisSessionManager;

import java.time.Duration;
import java.util.Map;

import static top.funsite.spring.action.shiro.configurers.NamedFilter.*;

/**
 * Shiro 配置类。
 * <ol>
 *     <li>Shiro 重定向默认带上 JSESSIONID？See {@link ShiroHttpServletResponse#encodeRedirectURL(String)}.</li>
 *     <li>使用 Bean 定义 Shiro Filter. See {@link ShiroFilterFactoryBean}.</li>
 * </ol>
 * <p>401 unauthenticated</p>
 * <p>403 unauthorized</p>
 *
 * @see AbstractShiroWebFilterConfiguration
 */
@Configuration
public class ShiroConfig {

    @Resource
    private ShiroProperties shiroProperties;

    @Getter
    private static RememberMe rememberMe;
    @Getter
    private static String loginUrl;
    @Getter
    private static Duration timeout;
    @Getter
    private static String sessionKeySeparator;

    @Getter
    @Setter
    public static class RememberMe {
        private boolean enabled;
        private Duration timeout;
    }

    /**
     * 解决 Shiro 权限注解不生效的问题。
     *
     * @return DefaultAdvisorAutoProxyCreator
     * @see RequiresRoles
     * @see RequiresPermissions
     * @see <a href="https://blog.csdn.net/m0_37890289/article/details/94014359">shiro使用注解鉴权时一直404</a>
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        // proxyCreator.setProxyTargetClass(true);
        proxyCreator.setUsePrefix(true);
        return proxyCreator;
    }

    /**
     * 默认的 Realm，Spring 环境下必须要有一个 Realm Bean.
     *
     * @param userService UserService
     * @return DatabaseRealm
     * @see ShiroAutoConfiguration
     */
    @Bean
    public Realm realm(UserService userService) {
        {
            var rememberProps = shiroProperties.getRemember();
            var remember = new RememberMe();
            remember.setEnabled(rememberProps.isEnabled());
            remember.setTimeout(rememberProps.getTimeout());

            rememberMe = remember;
            timeout = shiroProperties.getTimeout();
            loginUrl = shiroProperties.getLoginUrl();
            sessionKeySeparator = shiroProperties.getSessionKeySeparator();
        }
        return new DatabaseRealm(userService);
    }

    @Bean
    public DefaultWebSecurityManager securityManager(Realm realm, RedisTemplate<String, Object> redisTemplate) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /*ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        securityManager.setAuthenticator(authenticator);*/
        securityManager.setSessionManager(new RedisSessionManager(redisTemplate, shiroProperties.getSessionKeySeparator()));
        securityManager.setSubjectFactory(new RedisSubjectFactory());
        securityManager.setSubjectDAO(new RedisSubjectDAO());
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setLoginUrl(shiroProperties.getLoginUrl());
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        AuthorizeRequestsDefiner definer = createRequestsDefiner();
        Map<String, String> authRequestMap = definer.getDefinedPath();
        Map<String, Logical> authLogicMap = definer.getDefinedLogic();

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        // 使用重写过的过滤器代替默认的。
        filters.put(authc.name(), new AuthFilter());
        filters.put(remember.name(), new RememberedFilter());
        filters.put(roles.name(), new RoleFilter(authLogicMap));
        filters.put(perms.name(), new PermissionFilter(authLogicMap));
        filters.put(jwt.name(), new JwtFilter());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(authRequestMap);
        return shiroFilterFactoryBean;
    }

    /**
     * <p>配置要拦截接口（规则），例如：</p>
     *
     * <p>
     * {@code put("/access/role", "authc, roles[ROLE_A,ROLE_B]")}<br />
     * {@code put("/access/perm", "authc, perms[doc:read]")}
     * </p>
     * <p>{@code org.apache.catalina.core.StandardHostValve.java:166?#status(request, response)}</p>
     *
     * @see DefaultShiroFilterChainDefinition
     * @see <a href="https://www.jianshu.com/p/0bad726d0454">关于springMvc错误重定向/error</a>
     */
    private static AuthorizeRequestsDefiner createRequestsDefiner() {
        return new AuthorizeRequestsDefiner()
                .antMatchers("/login", "/error", "/favicon.ico").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/logout").rememberMe()
                .antMatchers("/info").rememberMe()
                .antMatchers("/home3").hasAnyRoles("home1", "home3")
                .antMatchers("/jwt").jwt()
                .antMatchers("/**").authenticated();
    }
}
