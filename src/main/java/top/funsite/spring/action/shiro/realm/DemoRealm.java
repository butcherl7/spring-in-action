package top.funsite.spring.action.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.User;

public class DemoRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String username = uToken.getUsername();
        char[] password = uToken.getPassword();

        String realmName = getName();

        User user = new User();
        user.setUsername(username);
        user.setPassword(String.valueOf(password));
        return new SimpleAuthenticationInfo(user, password, realmName);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(user.getRoles());
        info.setStringPermissions(user.getPermissions());
        return info;
    }
}
