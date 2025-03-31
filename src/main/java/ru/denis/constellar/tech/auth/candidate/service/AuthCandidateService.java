package ru.denis.constellar.tech.auth.candidate.service;

import jakarta.servlet.http.HttpSession;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface AuthCandidateService {

    void registerCandidate(RequestCandidateRegistration requestRegistration);

    void loginCandidate(RequestCandidateLogin requestLogin, HttpSession httpSession);
}
