package top.funsite.spring.action.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.ProxiedSession;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Subject，取消了对 session 的代理，在已登录的情况下调用 {@link Subject#getSession()} 直接获取到 {@link RedisSession}.
 *
 * @see ProxiedSession
 * @see DelegatingSubject#decorate
 */
public class RedisDelegatingSubject extends WebDelegatingSubject {

    public RedisDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, ServletRequest request, ServletResponse response, SecurityManager securityManager) {
        super(principals, authenticated, host, session, request, response, securityManager);
    }

    @Override
    protected Session decorate(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null.");
        }
        return session;
    }
}
