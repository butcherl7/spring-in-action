package top.funsite.spring.action.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.UserDTO;
import top.funsite.spring.action.domin.entity.User;
import top.funsite.spring.action.service.UserService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 从数据库检索用户并获取用户信息的 Realm.
 */
public class DatabaseRealm extends AbstractRealm {

    private final UserService userService;

    public DatabaseRealm(UserService userService) {
        super();
        this.userService = userService;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authToken;

        User user = userService.getByUsername(upToken.getUsername());

        if (user == null) {
            throw new UnknownAccountException();
        }

        String password = user.getPassword();
        LocalDateTime unlockedTime = user.getUnlockedTime();

        if (!user.isEnabled()) {
            throw new DisabledAccountException("This account has been disabled.");
        }
        if (unlockedTime != null) {
            long until = LocalDateTime.now().until(unlockedTime, ChronoUnit.SECONDS);
            if (until > 0) {
                long min = until >= 60 ? (long) Math.ceil(until / 60.0) : 1L;
                throw new LockedAccountException("Account locked, please try again in " + min + " minutes.");
            }
        }

        return new SimpleAuthenticationInfo(UserDTO.from(user), password, getName());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDTO userDTO = (UserDTO) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(userDTO.getRoles());
        info.setStringPermissions(userDTO.getPermissions());
        // if admin
        // info.setObjectPermissions(Collections.singleton(new AllPermission()));
        return info;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }
}
