package ru.denis.constellar.tech.auth.candidate.controller.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.auth.candidate.controller.AuthCandidateController;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;

import java.io.IOException;

@RestController
@RequestMapping("/constellar.tech/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthCandidateControllerImpl implements AuthCandidateController {

    @Value("${ip}")
    private String ip;
    private final AuthCandidateService authService;

    @PostMapping("/register-candidate")
    @Override
    public void registerCandidate(@Valid @RequestBody RequestCandidateRegistration requestRegistration) {

        authService.registerCandidate(requestRegistration);
    }

    @PostMapping("/login-candidate")
    @Override
    public void loginCandidate(@RequestBody RequestCandidateLogin requestLogin, HttpSession httpSession, HttpServletResponse response) {

        authService.loginCandidate(requestLogin, httpSession, response);

    }

    @Override
    @PostMapping("/logout-candidate")
    public void logoutCandidate(HttpSession httpSession, HttpServletResponse response) throws IOException {

        if (httpSession == null || httpSession.getAttribute("candidate") == null) {
            response.sendRedirect("http://" + ip + ":8080/home");
            return;
        }


        httpSession.removeAttribute("candidate");
        response.sendRedirect("http://" + ip + ":8080/home");

    }

}
