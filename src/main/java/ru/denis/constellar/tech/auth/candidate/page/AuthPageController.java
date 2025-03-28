package ru.denis.constellar.tech.auth.candidate.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    @GetMapping("/login-user-page")
    public String getLoginUserPage() {
        return "login-user";
    }

    @GetMapping("/register-user-page")
    public String getRegisterUserPage() {return "register-user";
    }
}
