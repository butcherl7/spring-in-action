package top.funsite.spring.action.domin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@TableName("sys_user")
public class User {

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
