package top.funsite.spring.action.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.User;

import java.util.HashSet;
import java.util.Set;

public class DemoRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String username = uToken.getUsername();
        char[] password = uToken.getPassword();

        if (StringUtils.isBlank(username)) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        if (username.length() > 6) {
            throw new UnknownAccountException("No account found for user [" + username + "]");
        }
        if (password.length > 10) {
            throw new IncorrectCredentialsException("Incorrect Credentials");
        }

        String realmName = getName();

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        User user = new User();
        user.setUsername(username);
        user.setPassword(String.valueOf(password));
        user.setRoles(roles);
        user.setPermissions(permissions);
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