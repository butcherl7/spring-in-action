package top.funsite.spring.action.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import top.funsite.spring.action.shiro.RedisSubjectFactory;
import top.funsite.spring.action.shiro.session.RedisSessionDAO;
import top.funsite.spring.action.shiro.session.RedisSessionManager;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.shiro.web.filter.mgt.DefaultFilter.anon;
import static org.apache.shiro.web.filter.mgt.DefaultFilter.authc;

/**
 * Shiro 配置类。
 * <ol>
 *     <li>Shiro 重定向为何默认带上 JSESSIONID？see {@link ShiroHttpServletResponse#encodeRedirectURL(String)}.</li>
 * </ol>
 * 使用 Bean 定义 Shiro Filter. see {@link ShiroFilterFactoryBean}.
 */
@Configuration
public class ShiroConfig {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public Realm realm() {
        TextConfigurationRealm realm = new TextConfigurationRealm();
        realm.setUserDefinitions("tom=password,user\n" + "jill.coder=password,admin");
        realm.setRoleDefinitions("admin=read,write\n" + "user=read");
        realm.setCachingEnabled(true);
        return realm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(@Autowired Realm realm) {
        RedisSessionDAO sessionDAO = new RedisSessionDAO(redisTemplate);

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(new RedisSessionManager(sessionDAO));
        securityManager.setSubjectFactory(new RedisSubjectFactory());
        securityManager.setRealm(realm);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/login");

        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    private static Map<String, String> filterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/login", anon.name());
        filterChainDefinitionMap.put("/**", authc.name());
        return filterChainDefinitionMap;
    }
}
