package top.funsite.spring.action.service;

import org.springframework.stereotype.Service;
import top.funsite.spring.action.domin.entity.User;
import top.funsite.spring.action.mapper.UserMapper;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

}
