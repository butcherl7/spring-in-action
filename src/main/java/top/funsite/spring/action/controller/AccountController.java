package top.funsite.spring.action.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.funsite.spring.action.service.LoginService;
import top.funsite.spring.action.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AccountController {

    @Resource
    private LoginService loginService;

    @GetMapping("login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request) {
        String requestIp = WebUtils.getRequestIp(request);
        return loginService.login(new UsernamePasswordToken(username, password, requestIp));
    }

    @GetMapping("info")
    public Object info() {
        return SecurityUtils.getSubject().getPrincipal();
    }

    @GetMapping("home1")
    public String home() {
        return "Welcome Home 1 😂";
    }

    @GetMapping("home2")
    @RequiresRoles("home2")
    public String home2() {
        return "Welcome Home 2 🤣";
    }
}
