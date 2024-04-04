package top.funsite.spring.action.log.annotation;

import java.lang.annotation.*;

/**
 * 记录接口请求信息的注解。
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {

    /**
     * 接口在日志记录的名称。
     *
     * @return 接口在日志记录的名称。
     */
    String name() default "";

    /**
     * 声明要记录的请求头（默认为空）。
     *
     * @return 要记录的请求头数组。
     */
    String[] headers() default {};

    /**
     * 是否记录请求参数（默认为 True）。
     *
     * @return True 表示记录。
     */
    boolean logRequest() default true;

    /**
     * 是否记录响应结果（默认为 True）。
     *
     * @return True 表示记录。
     */
    boolean logResponse() default true;
}
