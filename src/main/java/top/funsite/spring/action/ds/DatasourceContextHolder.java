package top.funsite.spring.action.ds;

public final class DatasourceContextHolder {

    private static final ThreadLocal<DsName> contextHolder = new ThreadLocal<>();

    public static void set(DsName dsName) {
        contextHolder.set(dsName);
    }

    public static DsName get() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }
}
