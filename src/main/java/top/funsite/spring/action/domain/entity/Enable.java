package top.funsite.spring.action.domain.entity;

public interface Enable {

    Boolean getEnabled();

    default boolean isEnabled() {
        return getEnabled() != null && getEnabled();
    }
}
