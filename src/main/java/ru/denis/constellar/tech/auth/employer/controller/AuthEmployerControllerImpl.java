package ru.denis.constellar.tech.auth.employer.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;
import ru.denis.constellar.tech.auth.employer.service.CompanyAuthService;

@RestController
@RequestMapping("/constellar.tech/api/v1/auth/employer")
@RequiredArgsConstructor
public class AuthEmployerControllerImpl {

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
}
