package top.funsite.spring.action.domain;

import lombok.Getter;
import lombok.Setter;
import top.funsite.spring.action.domain.entity.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * 表示已登录用户的信息。
 */
@Getter
@Setter
public class CurrentUser implements Serializable {

    @Serial
    private static final long serialVersionUID = -6580744408770092962L;

    private String username;

    private String password;

    private Set<String> roles;

    private Set<String> permissions;

    public static CurrentUser from(User user) {
        if (user == null) {
            return null;
        }
        CurrentUser currentUser = new CurrentUser();
        currentUser.setUsername(user.getUsername());
        currentUser.setPassword(user.getPassword());
        currentUser.setRoles(user.getRoles());
        currentUser.setPermissions(user.getPermissions());
        return currentUser;
    }
}
