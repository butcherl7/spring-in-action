package top.funsite.spring.action.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * @author Butcher
 */
@Getter
@Setter
public class Category {

    @Id
    private Integer id;
}
