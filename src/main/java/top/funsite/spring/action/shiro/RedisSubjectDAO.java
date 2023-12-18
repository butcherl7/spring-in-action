package top.funsite.spring.action.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.subject.Subject;

@Slf4j
public class RedisSubjectDAO extends DefaultSubjectDAO {

    @Override
    protected void saveToSession(Subject subject) {
        super.saveToSession(subject);
        // TODO 暂时这么做
        super.removeFromSession(subject);
    }
}
