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
import top.funsite.spring.action.domin.UserDTO;
import top.funsite.spring.action.shiro.session.RedisSession;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

@Slf4j
public class RedisSubjectFactory extends DefaultWebSubjectFactory {

    @Override
    public Subject createSubject(SubjectContext context) {
        WebSubjectContext wsc = (WebSubjectContext) context;

        String host = wsc.resolveHost();
        Session session = wsc.getSession();
        ServletRequest request = wsc.resolveServletRequest();
        ServletResponse response = wsc.resolveServletResponse();
        SecurityManager securityManager = wsc.resolveSecurityManager();

        PrincipalCollection principals = null;
        boolean timeout = false;

        if (session instanceof RedisSession) {
            UserDTO userDTO = (UserDTO) session.getAttribute(RedisSession.Key.user);
            String realmName = (String) session.getAttribute(RedisSession.Key.realmName);
            principals = new SimplePrincipalCollection(userDTO, realmName);
            timeout = ((RedisSession) session).isTimeout();
        } else {
            AuthenticationInfo authInfo = wsc.getAuthenticationInfo();
            if (authInfo != null) {
                principals = authInfo.getPrincipals();
            }
        }

        boolean authenticated = principals != null && (principals.getPrimaryPrincipal() instanceof UserDTO) && !timeout;

        return new RedisDelegatingSubject(principals, authenticated, host, session, request, response, securityManager);
    }
}
