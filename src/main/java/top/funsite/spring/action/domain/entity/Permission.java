package top.funsite.spring.action.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.funsite.spring.action.domain.entity.struct.Enable;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@TableName("sys_permission")
public class Permission implements Enable {

    @TableId(type = IdType.INPUT)
    private String name;

    private Boolean enabled;

    private LocalDateTime createdTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return StringUtils.equalsIgnoreCase(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}