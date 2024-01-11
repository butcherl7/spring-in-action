package top.funsite.spring.action.shiro.credential;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

/**
 * 使用 Bcrypt 加密算法验证传入的密码是否与系统中存储的相应帐户的密码匹配。
 */
public class BcryptPasswordMatcher extends SimpleCredentialsMatcher {

    private static final BCrypt.Verifyer verifyer = BCrypt.verifyer();

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String submittedPassword = toString(token.getCredentials());
        String storedCredentials = toString(info.getCredentials());
        return verifyer.verify(submittedPassword.toCharArray(), storedCredentials.toCharArray()).verified;
    }
}
