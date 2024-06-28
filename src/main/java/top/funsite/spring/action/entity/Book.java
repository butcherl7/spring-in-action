package top.funsite.spring.action.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Book {

    private Long id;

    private String name;

    private String createdBy;

    private LocalDateTime createdAt;
}
