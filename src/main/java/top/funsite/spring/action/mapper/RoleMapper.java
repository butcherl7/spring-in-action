package top.funsite.spring.action.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.funsite.spring.action.domin.entity.Role;

import java.util.Set;

public interface RoleMapper extends BaseMapper<Role> {

    @Select("""
            select a.name
            from sys_role a,
                 sys_user_role b
            where a.name = b.role_name
              and a.enabled is true
              and b.uid = #{uid}
            """)
    Set<String> selectUserRoles(long uid);
}
