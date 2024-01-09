package top.funsite.spring.action.shiro.realm;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.UserDTO;

import java.util.HashSet;
import java.util.Set;

/**
 * 从数据库检索用户并获取用户信息的 Realm.
 */
public class DatabaseRealm extends AuthorizingRealm {

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

        Set<String> roles = new HashSet<>();
        Set<String> permissions = new HashSet<>();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(String.valueOf(password));
        userDTO.setRoles(roles);
        userDTO.setPermissions(permissions);
        return new SimpleAuthenticationInfo(userDTO, password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDTO userDTO = (UserDTO) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(userDTO.getRoles());
        info.setStringPermissions(userDTO.getPermissions());
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
