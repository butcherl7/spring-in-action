package top.funsite.spring.action.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import top.funsite.spring.action.domin.entity.Permission;

import java.util.Set;

public interface PermissionMapper extends BaseMapper<Permission> {

    @Select("""
            select permission_name
            from sys_role_permission
            where role_name in (select role_name from sys_user_role where uid = #{uid})
            union all
            select permission_name
            from sys_user_permission
            where uid = #{uid}
            """)
    Set<String> selectPermissionsNameByUID(long uid);
}
