package top.funsite.spring.action.config;

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
import top.funsite.spring.action.shiro.realm.DemoRealm;
import top.funsite.spring.action.shiro.session.RedisSessionManager;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.anon;
import static org.apache.shiro.web.filter.mgt.DefaultFilter.authc;

/**
 * Shiro 配置类。
 * <ol>
 *     <li>Shiro 重定向为何默认带上 JSESSIONID？See {@link ShiroHttpServletResponse#encodeRedirectURL(String)}.</li>
 * </ol>
 * 使用 Bean 定义 Shiro Filter. See {@link ShiroFilterFactoryBean}.
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
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setUsePrefix(true);
        return proxyCreator;
    }

    @Bean
    public Realm realm() {
        return new DemoRealm();
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

        // Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    /**
     * 过滤器链的定义，例如：
     * <p>{@code xxx.put("/access/role", "authc, roles[ROLE_A,ROLE_B]")}</p>
     * <p>{@code xxx.put("/access/perm", "authc, perms[doc:read]")}</p>
     * <p>或者使用 {@link DefaultShiroFilterChainDefinition}.</p>
     */
    private static Map<String, String> filterChainDefinitionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("/login", anon.name());
        // https://www.jianshu.com/p/0bad726d0454
        // org.apache.catalina.core.StandardHostValve.java:166 status(request, response);
        map.put("/error", anon.name());
        map.put("/**", authc.name());
        return map;
    }
}
