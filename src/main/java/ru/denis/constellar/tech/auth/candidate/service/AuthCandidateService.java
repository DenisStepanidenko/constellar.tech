package ru.denis.constellar.tech.auth.candidate.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface AuthCandidateService {

    void registerCandidate(RequestCandidateRegistration requestRegistration);

    void loginCandidate(RequestCandidateLogin requestLogin, HttpSession httpSession, HttpServletResponse response);
}
