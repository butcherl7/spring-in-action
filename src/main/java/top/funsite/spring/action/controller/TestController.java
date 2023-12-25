package top.funsite.spring.action.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.log.RequestLog;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @PostMapping("/books")
    @RequestLog(name = "Books")
    public ResponseEntity<Map<String, Object>> books(@RequestBody Map<String, Object> map) {
        Map<String, Object> entity = new HashMap<>(16);
        entity.put("name", "Haha");
        return ResponseEntity.ok(entity);
    }

    @GetMapping("/users")
    @RequestLog(name = "Users", headers = {"Token", "Ha"})
    public ResponseEntity<Map<String, Object>> users(User user, HttpServletRequest request) {
        Map<String, Object> entity = new HashMap<>(16);
        entity.put("name", "Haha");
        return ResponseEntity.ok(entity);
    }

}
