package top.funsite.spring.action.shiro.credential;

import org.apache.shiro.authc.credential.PasswordMatcher;

public class StrongerHashMatcher extends PasswordMatcher {

    public StrongerHashMatcher() {
        setPasswordService(new StrongerHashPasswordService());
    }
}
