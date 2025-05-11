package ru.denis.constellar.tech.auth.candidate.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthCandidateServiceImplTest {

    @Mock
    private CandidateService candidateService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpSession httpSession;

    @Mock
    private HttpServletResponse httpResponse;

    @InjectMocks
    private AuthCandidateServiceImpl authService;

    @Test
    void registerCandidate_ValidRequest_CallsService() {
        // GIVEN
        RequestCandidateRegistration request = new RequestCandidateRegistration();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setConfirmPassword("Password123!");
        when(candidateService.isExistsByEmail("test@example.com")).thenReturn(false);

        // WHEN
        authService.registerCandidate(request);

        // THEN
        verify(candidateService, times(1)).isExistsByEmail("test@example.com");
        verify(candidateService, times(1)).register(request);
        verifyNoMoreInteractions(candidateService);
    }

    @Test
    void registerCandidate_EmailAlreadyExists_ThrowsException() {
        // GIVEN
        RequestCandidateRegistration request = new RequestCandidateRegistration();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        request.setConfirmPassword("Password123!");
        when(candidateService.isExistsByEmail("test@example.com")).thenReturn(true);

        // WHEN + THEN
        assertThrows(EmailAlreadyExists.class, () -> authService.registerCandidate(request),
                "Должно быть выброшено EmailAlreadyExists");
        verify(candidateService, times(1)).isExistsByEmail("test@example.com");
        verify(candidateService, never()).register(any());
    }

    @Test
    void loginCandidate_ValidCredentials_SetsSessionAndRedirects() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        Candidate candidate = new Candidate();
        candidate.setEmail("test@example.com");
        candidate.setPassword("encodedPassword");
        when(candidateService.findByEmail("test@example.com")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("Password123!", "encodedPassword")).thenReturn(true);

        // WHEN
        authService.loginCandidate(request, httpSession, httpResponse);

        // THEN
        verify(candidateService, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("Password123!", "encodedPassword");
        verify(httpSession, times(1)).setAttribute("candidate", candidate);
        verify(httpResponse, times(1)).sendRedirect("http://localhost:8080/candidate-personal-account");
    }

    @Test
    void loginCandidate_EmailNotFound_ThrowsException() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        when(candidateService.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(PasswordOrEmailIncorrect.class, () ->
                        authService.loginCandidate(request, httpSession, httpResponse),
                "Должно быть выброшено PasswordOrEmailIncorrect");
        verify(candidateService, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(httpSession, never()).setAttribute(anyString(), any());
        verify(httpResponse, never()).sendRedirect(anyString());
    }

    @Test
    void loginCandidate_IncorrectPassword_ThrowsException() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");
        Candidate candidate = new Candidate();
        candidate.setEmail("test@example.com");
        candidate.setPassword("encodedPassword");
        when(candidateService.findByEmail("test@example.com")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        // WHEN + THEN
        assertThrows(PasswordOrEmailIncorrect.class, () ->
                        authService.loginCandidate(request, httpSession, httpResponse),
                "Должно быть выброшено PasswordOrEmailIncorrect");
        verify(candidateService, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("wrongpassword", "encodedPassword");
        verify(httpSession, never()).setAttribute(anyString(), any());
        verify(httpResponse, never()).sendRedirect(anyString());
    }

    @Test
    void loginCandidate_IOExceptionDuringRedirect_ThrowsRuntimeException() throws IOException {
        // GIVEN
        RequestCandidateLogin request = new RequestCandidateLogin();
        request.setEmail("test@example.com");
        request.setPassword("Password123!");
        Candidate candidate = new Candidate();
        candidate.setEmail("test@example.com");
        candidate.setPassword("encodedPassword");
        when(candidateService.findByEmail("test@example.com")).thenReturn(Optional.of(candidate));
        when(passwordEncoder.matches("Password123!", "encodedPassword")).thenReturn(true);
        doThrow(new IOException("Redirect failed")).when(httpResponse).sendRedirect(anyString());

        // WHEN + THEN
        assertThrows(RuntimeException.class, () ->
                        authService.loginCandidate(request, httpSession, httpResponse),
                "Должно быть выброшено RuntimeException");
        verify(candidateService, times(1)).findByEmail("test@example.com");
        verify(passwordEncoder, times(1)).matches("Password123!", "encodedPassword");
        verify(httpSession, times(1)).setAttribute("candidate", candidate);
        verify(httpResponse, times(1)).sendRedirect("http://localhost:8080/candidate-personal-account");
    }
}