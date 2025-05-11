package ru.denis.constellar.tech.auth.candidate.controller.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCandidateControllerImplTest {

    @Mock
    private AuthCandidateService authService;

    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletResponse httpResponse;

    @InjectMocks
    private AuthCandidateControllerImpl authController;

    @Test
    void registerCandidate_ValidRequest_CallsService() {
        // GIVEN
        RequestCandidateRegistration request = new RequestCandidateRegistration();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setConfirmPassword("Password123!");

        // WHEN
        authController.registerCandidate(request);

        // THEN
        verify(authService, times(1)).registerCandidate(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void registerCandidate_EmailAlreadyExists_ThrowsException() {
        // GIVEN
        RequestCandidateRegistration request = new RequestCandidateRegistration();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setConfirmPassword("Password123!");
        doThrow(new EmailAlreadyExists("Аккаунт с данным email уже зарегистрирован."))
                .when(authService).registerCandidate(request);

        // WHEN + THEN
        assertThrows(EmailAlreadyExists.class, () -> authController.registerCandidate(request));
        verify(authService, times(1)).registerCandidate(request);
    }

    @Test
    void loginCandidate_ValidRequest_CallsService() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");

        // WHEN
        authController.loginCandidate(request, httpSession, httpResponse);

        // THEN
        verify(authService, times(1)).loginCandidate(request, httpSession, httpResponse);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void loginCandidate_IncorrectCredentials_ThrowsException() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");
        doThrow(new PasswordOrEmailIncorrect("Пароль или почта введены неверно."))
                .when(authService).loginCandidate(request, httpSession, httpResponse);

        // WHEN + THEN
        assertThrows(PasswordOrEmailIncorrect.class, () ->
                authController.loginCandidate(request, httpSession, httpResponse));
        verify(authService, times(1)).loginCandidate(request, httpSession, httpResponse);
    }

    @Test
    void logoutCandidate_NoCandidateInSession_RedirectsToHome() throws IOException {
        // GIVEN
        when(httpSession.getAttribute("candidate")).thenReturn(null);

        // WHEN
        authController.logoutCandidate(httpSession, httpResponse);

        // THEN
        verify(httpResponse, times(1)).sendRedirect("http://localhost:8080/home");
        verify(httpSession, never()).removeAttribute("candidate");
    }

    @Test
    void logoutCandidate_CandidateInSession_RemovesAttributeAndRedirects() throws IOException {
        // GIVEN
        Object candidate = new Object();
        when(httpSession.getAttribute("candidate")).thenReturn(candidate);

        // WHEN
        authController.logoutCandidate(httpSession, httpResponse);

        // THEN
        verify(httpSession, times(1)).removeAttribute("candidate");
        verify(httpResponse, times(1)).sendRedirect("http://localhost:8080/home");
    }

    @Test
    void logoutCandidate_NullSession_RedirectsToHome() throws IOException {
        // WHEN
        authController.logoutCandidate(null, httpResponse);

        // THEN
        verify(httpResponse, times(1)).sendRedirect("http://localhost:8080/home");
        verifyNoInteractions(httpSession);
    }
}