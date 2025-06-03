package ru.denis.constellar.tech.auth.employer.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CompanyAuthService {

    @Value("${ip}")
    private String ip;
    private final EmployerJpa employerJpa;
    private final PasswordEncoder passwordEncoder;

    public void loginCompany(RequestCompanyLogin requestLogin, HttpSession session, HttpServletResponse response) {

        Employer employer = employerJpa.findByEmail(requestLogin.getEmail()).orElseThrow(() -> new PasswordOrEmailIncorrect("Пароль или почта введены неверно."));

        String encodedPassword = employer.getPassword();

        if (!passwordEncoder.matches(requestLogin.getPassword(), encodedPassword)) {
            throw new PasswordOrEmailIncorrect("Пароль или почта введены неверно.");
        }

        session.setAttribute("employer", employer);


        try {
            response.sendRedirect("http://" + ip + ":8080/company-personal-account-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void registerCompany(CompanyRegistrationRequest companyRegistrationRequest) {

        boolean isThisEmailAlreadyExists = employerJpa.existsByEmail(companyRegistrationRequest.getEmail());


        if (isThisEmailAlreadyExists) {
            throw new EmailAlreadyExists("Аккаунт с данным email уже зарегистрирован.");
        }

        Employer employer = Employer.builder()
                .email(companyRegistrationRequest.getEmail())
                .password(passwordEncoder.encode(companyRegistrationRequest.getPassword()))
                .companyName(companyRegistrationRequest.getCompanyName())
                .inn(companyRegistrationRequest.getInn())
                .build();

        employerJpa.save(employer);
    }
}
