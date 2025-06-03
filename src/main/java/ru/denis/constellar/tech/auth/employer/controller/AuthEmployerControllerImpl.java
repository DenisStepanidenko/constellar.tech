package ru.denis.constellar.tech.auth.employer.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;
import ru.denis.constellar.tech.auth.employer.service.CompanyAuthService;

import java.io.IOException;

@RestController
@RequestMapping("/constellar.tech/api/v1/auth/employer")
@RequiredArgsConstructor
public class AuthEmployerControllerImpl {

    @Value("${ip}")
    private String ip;
    private final CompanyAuthService companyAuthService;

    @PostMapping("/login")
    public void login(@RequestBody RequestCompanyLogin requestLogin,
                      HttpSession session,
                      HttpServletResponse response) {

        companyAuthService.loginCompany(requestLogin, session, response);
    }

    @PostMapping("/register")
    public void register(@RequestBody @Valid CompanyRegistrationRequest companyRegistrationRequest) {
        companyAuthService.registerCompany(companyRegistrationRequest);
    }

    @PostMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) throws IOException {

        if(session == null || session.getAttribute("candidate") == null){
            response.sendRedirect("http://" + ip + ":8080/home");
            return;
        }


        session.removeAttribute("employer");
        response.sendRedirect("http://" + ip + ":8080/home");

    }
}
