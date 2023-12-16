package top.funsite.spring.action.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RedisSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        WebSubjectContext wsc = (WebSubjectContext) context;

        ServletRequest request = wsc.resolveServletRequest();
        ServletResponse response = wsc.resolveServletResponse();
        SecurityManager securityManager = wsc.resolveSecurityManager();

        Session session = context.getSession();
        boolean authenticated = session instanceof RedisSession;
        PrincipalCollection principals = authenticated ? new SimplePrincipalCollection("", "") : null;

        return new WebDelegatingSubject(principals, authenticated, "", session, request, response, securityManager);
    }
}
