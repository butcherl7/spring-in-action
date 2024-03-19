package top.funsite.spring.action.domin.entity.struct;

public interface Enable {

    Boolean getEnabled();

    default boolean isEnabled() {
        return getEnabled() != null && getEnabled();
    }
}
