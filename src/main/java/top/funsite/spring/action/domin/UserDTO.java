package top.funsite.spring.action.domin;

import lombok.Getter;
import lombok.Setter;

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
}
