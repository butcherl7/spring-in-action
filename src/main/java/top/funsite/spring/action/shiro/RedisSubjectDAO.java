package top.funsite.spring.action.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

@Slf4j
public class RedisSubjectDAO extends DefaultSubjectDAO {

    @Override
    protected void mergePrincipals(Subject subject) {
        super.mergePrincipals(subject);
        Session session = subject.getSession();
        if (session != null) {
            // TODO 暂时这么做
            session.removeAttribute(DefaultSubjectContext.AUTHENTICATED_SESSION_KEY);
            session.removeAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        }
    }
}
