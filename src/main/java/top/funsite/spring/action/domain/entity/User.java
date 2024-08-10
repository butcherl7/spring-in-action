package top.funsite.spring.action.domain.entity;

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
public class User implements Enable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime unlockedTime;

    private Boolean enabled;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @TableField(exist = false)
    private Set<String> roles;

    @TableField(exist = false)
    private Set<String> permissions;
}
