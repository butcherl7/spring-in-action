package top.funsite.spring.action.shiro.credential;

import org.apache.shiro.authc.credential.PasswordMatcher;

/**
 * 使用 Bcrypt 加密算法验证传入的密码是否与系统中存储的相应帐户的密码匹配。
 */
public class BcryptPasswordMatcher extends PasswordMatcher {

    public BcryptPasswordMatcher() {
        setPasswordService(new StrongerHashPasswordService());
    }
}
