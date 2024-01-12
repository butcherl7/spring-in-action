package top.funsite.spring.action.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.ProxiedSession;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Subject，取消了对 session 的代理。
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
        if (!(session instanceof RedisSession)) {
            throw new IllegalArgumentException("session must be redis session.");
        }
        return session;
    }
}
