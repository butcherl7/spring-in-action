package top.funsite.spring.action.domin;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private String username;

    private String password;

    private Set<String> roles;

    private Set<String> permissions;
}
