package top.funsite.spring.action.log;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLog {

    String name() default "";

    String[] headers() default {};

}
