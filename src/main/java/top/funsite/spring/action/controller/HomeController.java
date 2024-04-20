package top.funsite.spring.action.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.funsite.spring.action.domain.User;

import java.time.LocalDateTime;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (!(session.getAttribute("user") instanceof User)) {
            User user = new User();
            user.setFirstName("Gwendolyn");
            user.setLastName("Black");
            user.setNationality("USA");
            session.setAttribute("user", user);
        }

        model.addAttribute("today", LocalDateTime.now());
        model.addAttribute("greeting", "Xi");
        model.addAttribute("username", "Son Goku");
        model.addAttribute("welcomeMsgKey", "home.welcome");
        return "home";
    }
}
