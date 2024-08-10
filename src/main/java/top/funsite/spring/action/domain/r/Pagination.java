package top.funsite.spring.action.domain.r;

import java.util.Collections;
import java.util.List;

/**
 * 分页结构。
 *
 * @param total 数据总数。
 * @param list  当前页的数据。
 * @param <T>   数据类型。
 * @author Butcher
 */
public record Pagination<T>(long total, List<T> list) implements IPagination<T> {

    private static final Pagination<?> EMPTY_PAGINATION = new Pagination<>(0, Collections.emptyList());

    @SuppressWarnings("unchecked")
    public static <T> Pagination<T> empty() {
        return (Pagination<T>) EMPTY_PAGINATION;
    }
}
