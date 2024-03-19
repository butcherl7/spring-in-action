package top.funsite.spring.action.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.UserDTO;
import top.funsite.spring.action.domin.entity.User;
import top.funsite.spring.action.service.UserService;

import java.time.LocalDateTime;

import static top.funsite.spring.action.util.JSONUtils.DATE_TIME_FORMATTER;

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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        User user = userService.getByUsername((String) token.getPrincipal());

        if (user == null) {
            throw new UnknownAccountException();
        }

        String password = user.getPassword();
        LocalDateTime unlockedTime = user.getUnlockedTime();

        if (!user.isEnabled()) {
            throw new DisabledAccountException("账号已被禁用，详情请咨讠");
        }
        if (unlockedTime != null && unlockedTime.isAfter(LocalDateTime.now())) {
            throw new LockedAccountException("账号被锁定，请在 " + unlockedTime.format(DATE_TIME_FORMATTER) + " 后再试");
        }

        return new SimpleAuthenticationInfo(UserDTO.from(user), password, getName());
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
