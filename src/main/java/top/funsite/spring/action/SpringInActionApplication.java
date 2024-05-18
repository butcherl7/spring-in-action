package top.funsite.spring.action;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;

import java.util.Optional;

@Slf4j
@EnableJdbcAuditing
@SpringBootApplication
public class SpringInActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringInActionApplication.class, args);
    }

    @Bean
    AuditorAware<String> auditorProvider() {
        return () -> Optional.of("Test");
    }

    @Bean
    BeforeConvertCallback<?> beforeConvertCallback() {
        return (entity) -> {
            /*if (entity instanceof Commentable c) {
                c.setComment(System.currentTimeMillis() + "");
            }*/
            return entity;
        };
    }

    /*@Bean
    public BeforeSaveCallback<?> beforeSaveCallback() {
        return (entity, aggregateChange) -> {
            if (entity instanceof UpdateAt e) {
                e.setUpdateAt(LocalDateTime.now());
            }
            return entity;
        };
    }*/
}
