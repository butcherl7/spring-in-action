package top.funsite.spring.action.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.funsite.spring.action.domin.entity.Permission;

import java.util.Set;

public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("""
            select a.name
            from sys_permission a,
                 sys_user_permission b
            where a.name = b.permission_name
              and a.enabled is true
              and b.uid = #{uid}
            union all
            select b.name
            from sys_role a,
                 sys_permission b,
                 sys_user_role c,
                 sys_role_permission d
            where a.name = c.role_name
              and a.name = d.role_name
              and b.name = d.permission_name
              and a.enabled is true
              and b.enabled is true
              and c.uid = #{uid}
            """)
    Set<String> selectPermissionsNameByUID(long uid);
}
