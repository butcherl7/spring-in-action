package top.funsite.spring.action.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.funsite.spring.action.domin.entity.User;

public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(String username);

}
