package top.funsite.spring.action.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.funsite.spring.action.domin.entity.User;
import top.funsite.spring.action.mapper.PermissionMapper;
import top.funsite.spring.action.mapper.RoleMapper;
import top.funsite.spring.action.mapper.UserMapper;

import java.util.Set;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    public User getByUsername(String username) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return null;
        }

        Set<String> roles = roleMapper.selectRolesNameByUID(user.getId());
        Set<String> permissions = permissionMapper.selectPermissionsNameByUID(user.getId());

        user.setRoles(roles);
        user.setPermissions(permissions);
        return user;
    }

}
