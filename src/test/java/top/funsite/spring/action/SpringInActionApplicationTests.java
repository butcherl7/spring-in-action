package top.funsite.spring.action;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import top.funsite.spring.action.entity.Book;

import java.util.List;

@SpringBootTest
class SpringInActionApplicationTests {

    @Resource
    private JdbcClient jdbcClient;

    @Test
    public void testQueryBook() {
        // DatasourceContextHolder.set(DsName.TEST2);
        List<Book> books = jdbcClient.sql("select * from book")
                .query(Book.class)
                .list();
        books.forEach(System.out::println);
    }

    @Test
    public void testSaveBook() {
        int rows = jdbcClient.sql("insert into test1.book (name) values (?)")
                .params("hello World")
                .update();
        System.out.printf("%d row effected\n", rows);
    }

}
