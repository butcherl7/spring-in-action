package top.funsite.spring.action.domain.web;

import java.util.List;

/**
 * 分页数据的结构定义。
 *
 * @param <T> 数据类型。
 */
public interface IPagination<T> {

    /**
     * 返回数据总数。
     *
     * @return 数据总数。
     */
    long total();

    /**
     * 返回当前页的数据。
     *
     * @return 当前页的数据。
     */
    List<T> list();
}
