package top.funsite.spring.action.util;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SpringBeanUtils implements ApplicationContextAware {

    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    private SpringBeanUtils() {
    }

    /**
     * 返回指定 bean 的一个实例，该实例可以是共享的，也可以是独立的。
     *
     * @param name 要检索的 bean 的名称。
     * @param <T>  bean 类型。
     * @return bean 的实例。
     */
    public static <T> T getBean(String name) {
        return getBean(name, null);
    }

    /**
     * 返回唯一匹配给定对象类型的 bean 实例（如果有的话）。
     *
     * @param requiredType 键入 bean 必须匹配的类型，可以是接口或超类。
     * @param <T>          bean 类型。
     * @return 与所需类型匹配的单个 bean 的实例。
     */
    public static <T> T getBean(Class<T> requiredType) {
        return getBean(null, requiredType);
    }

    /**
     * 返回指定 bean 的一个实例，根据名称或类型（名称优先于类型）或是两者同时匹配（在都不为 null 的情况下）。
     *
     * @param name         要检索的 bean 的名称。
     * @param requiredType 键入 bean 必须匹配的类型，可以是接口或超类。
     * @param <T>          bean 类型。
     * @return bean 的实例。
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name, Class<T> requiredType) {
        Objects.requireNonNull(applicationContext);

        if (StringUtils.isNotBlank(name) && requiredType != null) {
            return applicationContext.getBean(name, requiredType);
        }
        if (StringUtils.isNotBlank(name)) {
            return (T) applicationContext.getBean(name);
        }
        if (requiredType != null) {
            return applicationContext.getBean(requiredType);
        }
        throw new IllegalArgumentException("name and requiredType cannot be all null.");
    }
}
