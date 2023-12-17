package top.funsite.spring.action.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.apache.shiro.web.subject.WebSubjectContext;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import top.funsite.spring.action.domin.User;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class RedisSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        WebSubjectContext wsc = (WebSubjectContext) context;

        Session session = wsc.getSession();
        ServletRequest request = wsc.resolveServletRequest();
        ServletResponse response = wsc.resolveServletResponse();
        SecurityManager securityManager = wsc.resolveSecurityManager();

        PrincipalCollection principals = null;

        if (session instanceof RedisSession) {
            User user = (User) session.getAttribute(RedisSession.Key.user);
            String realmName = (String) session.getAttribute(RedisSession.Key.realmName);
            principals = new SimplePrincipalCollection(user, realmName);
        } else {
            AuthenticationInfo authInfo = wsc.getAuthenticationInfo();
            if (authInfo != null) {
                principals = authInfo.getPrincipals();
            }
        }

        boolean authenticated = principals != null && principals.getPrimaryPrincipal() instanceof User;

        return new WebDelegatingSubject(principals, authenticated, "", session, request, response, securityManager);
    }
}
