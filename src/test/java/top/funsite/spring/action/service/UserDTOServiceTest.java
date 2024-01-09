package top.funsite.spring.action.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.funsite.spring.action.domin.entity.User;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserDTOServiceTest {

    @Resource
    private UserService userService;

    @Test
    void getByUsername() {
        User user = userService.getByUsername("admin");
        assertNotNull(user);
    }
}