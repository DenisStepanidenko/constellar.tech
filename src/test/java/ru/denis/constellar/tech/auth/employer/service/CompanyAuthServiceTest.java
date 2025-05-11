package ru.denis.constellar.tech.auth.employer.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyAuthServiceTest {

    @Mock
    private EmployerJpa employerJpa;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private CompanyAuthService companyAuthService;

    @Test
    void loginCompany_Success() throws Exception {
        // Arrange
        RequestCompanyLogin request = new RequestCompanyLogin("test@mail.com", "password");
        Employer employer = Employer.builder()
                .email("test@mail.com")
                .password("encodedPass")
                .build();

        when(employerJpa.findByEmail("test@mail.com")).thenReturn(Optional.of(employer));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);

        // Act
        companyAuthService.loginCompany(request, session, response);

        // Assert
        verify(session).setAttribute("employer", employer);
        verify(response).sendRedirect("http://localhost:8080/company-personal-account-home");
    }

    @Test
    void loginCompany_WrongPassword_ThrowsException() {
        RequestCompanyLogin request = new RequestCompanyLogin("test@mail.com", "wrong");
        Employer employer = new Employer();
        employer.setPassword("encodedPass");

        when(employerJpa.findByEmail("test@mail.com")).thenReturn(Optional.of(employer));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        assertThrows(PasswordOrEmailIncorrect.class, () ->
                companyAuthService.loginCompany(request, session, response));
    }

    @Test
    void registerCompany_Success() {
        CompanyRegistrationRequest request = new CompanyRegistrationRequest(
                "Company", "1234567890", "test@mail.com", "password", "password");

        when(employerJpa.existsByEmail("test@mail.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");

        companyAuthService.registerCompany(request);

        verify(employerJpa).save(argThat(employer ->
                employer.getEmail().equals("test@mail.com") &&
                        employer.getPassword().equals("encodedPass")));
    }

    @Test
    void registerCompany_EmailExists_ThrowsException() {
        CompanyRegistrationRequest request = new CompanyRegistrationRequest(
                "Company", "1234567890", "exists@mail.com", "password", "password");

        when(employerJpa.existsByEmail("exists@mail.com")).thenReturn(true);

        assertThrows(EmailAlreadyExists.class, () ->
                companyAuthService.registerCompany(request));
    }
}