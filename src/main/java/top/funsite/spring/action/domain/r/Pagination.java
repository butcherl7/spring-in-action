package top.funsite.spring.action.domain.r;

import java.util.List;

/**
 * 分页结构。
 *
 * @param total 数据总数。
 * @param list  数据集合。
 * @param <T>   数据类型。
 * @author Butcher
 */
public record Pagination<T>(long total, List<T> list) {
}
