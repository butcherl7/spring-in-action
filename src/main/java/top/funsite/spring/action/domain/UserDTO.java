package top.funsite.spring.action.domain;

import lombok.Getter;
import lombok.Setter;
import top.funsite.spring.action.domain.entity.User;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -6580744408770092962L;

    private String username;

    private String password;

    private Set<String> roles;

    private Set<String> permissions;

    public static UserDTO from(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoles(user.getRoles());
        userDTO.setPermissions(user.getPermissions());
        return userDTO;
    }
}
