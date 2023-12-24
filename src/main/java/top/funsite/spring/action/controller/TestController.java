package top.funsite.spring.action.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Map<String, Object>> get(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        Map<String, Object> entity = new HashMap<>(16);
        entity.put("name", "Haha");
        return ResponseEntity.ok(entity);
    }

}
