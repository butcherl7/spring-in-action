package top.funsite.spring.action.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import top.funsite.spring.action.domain.entity.struct.Enable;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@TableName("sys_user")
public class User implements Enable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime unlockedTime;

    private Boolean enabled;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    @TableField(exist = false)
    private Set<String> roles;

    @TableField(exist = false)
    private Set<String> permissions;
}
