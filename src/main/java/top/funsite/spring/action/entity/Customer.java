package top.funsite.spring.action.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.InsertOnlyProperty;
import top.funsite.spring.action.entity.s.Commentable;

import java.time.LocalDateTime;

/**
 * @author Butcher
 */
@Getter
@Setter
@Builder
public class Customer implements Commentable {

    @Id
    private Integer id;

    private String name;

    private Boolean gender;

    private String comment;

    @CreatedBy
    @InsertOnlyProperty
    private String createBy;

    @CreatedDate
    @InsertOnlyProperty
    private LocalDateTime createAt;

    @LastModifiedBy
    private String updateBy;

    @LastModifiedDate
    private LocalDateTime updateAt;
}
