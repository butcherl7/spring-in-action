package top.funsite.spring.action;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.funsite.spring.action.mapper")
public class SpringInActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInActionApplication.class, args);
    }

}
