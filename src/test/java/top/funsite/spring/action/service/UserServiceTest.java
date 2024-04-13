package top.funsite.spring.action.service;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.funsite.spring.action.domain.entity.User;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void getByUsername() {
        User user = userService.getByUsername("admin");
        assertNotNull(user);
    }
}
