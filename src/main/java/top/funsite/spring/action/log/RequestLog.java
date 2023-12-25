package top.funsite.spring.action.log;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLog {

    String name() default "";

    /**
     * 声明要记录的请求头。
     *
     * @return array
     */
    String[] headers() default {};

    /**
     * 是否记录 URL 中包含的查询字符串。
     *
     * @return bool
     */
    boolean logQueryString() default true;

    /**
     * 是否记录请求体。
     *
     * @return bool
     */
    boolean logRequest() default true;

    /**
     * 是否记录响应体。
     *
     * @return bool
     */
    boolean logResponse() default true;
}
