package top.funsite.spring.action.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import top.funsite.spring.action.log.annotation.ApiLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/books")
    @ApiLog(name = "Books")
    public ResponseEntity<Map<String, Object>> books(@RequestParam Map<String, Object> map) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/users")
    @ApiLog(name = "Users", headers = {"Token", "Ha"})
    public ResponseEntity<Map<String, Object>> users(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        log.debug(request.getRequestURI());
        int i = 1 / Integer.parseInt(String.valueOf(map.get("num")));
        log.debug(String.valueOf(i));

        Map<String, Object> entity = new HashMap<>(16);
        entity.put("name", "Haha");
        return ResponseEntity.ok(entity);
    }

}
