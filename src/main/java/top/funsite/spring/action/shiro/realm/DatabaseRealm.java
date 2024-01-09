package top.funsite.spring.action.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import top.funsite.spring.action.domin.UserDTO;
import top.funsite.spring.action.domin.entity.Permission;
import top.funsite.spring.action.domin.entity.Role;
import top.funsite.spring.action.domin.entity.User;
import top.funsite.spring.action.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static top.funsite.spring.action.util.JsonUtils.DATE_TIME_FORMATTER;

/**
 * 从数据库检索用户并获取用户信息的 Realm.
 */
public class DatabaseRealm extends AuthorizingRealm {

    private final UserService userService;

    public DatabaseRealm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken uToken = (UsernamePasswordToken) token;
        String username = uToken.getUsername();
        char[] cPassword = uToken.getPassword();

        String password = String.valueOf(cPassword);

        User user = userService.getByUsername(username);

        if (user == null) {
            throw new UnknownAccountException();
        }

        LocalDateTime unlockedTime = user.getUnlockedTime();

        if (unlockedTime != null && unlockedTime.isAfter(LocalDateTime.now())) {
            throw new LockedAccountException("账号被锁定，请在 " + unlockedTime.format(DATE_TIME_FORMATTER) + " 后再试");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException();
        }

        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<String> permissions = user.getRoles().stream().map(Role::getPermissions).flatMap(Collection::stream).map(Permission::getName).collect(Collectors.toSet());

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
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
