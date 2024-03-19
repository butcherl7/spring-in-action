package top.funsite.spring.action.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.funsite.spring.action.domin.entity.Role;

import java.util.Set;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("select role_name from sys_user_role where uid = #{uid}")
    Set<String> selectRolesNameByUID(long uid);
}
