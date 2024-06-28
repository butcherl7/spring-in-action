package top.funsite.spring.action.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.entity.Book;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("book")
public class BookController {

    @Resource
    private JdbcClient jdbcClient;

    @GetMapping("list")
    public List<Book> books() {
        log.info("list books.");
        return jdbcClient.sql("select * from book")
                .query(Book.class)
                .list();
    }
}
