package ru.denis.constellar.tech.auth.candidate.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.validation.BindingResult;
import ru.denis.constellar.tech.auth.candidate.dto.RegistrationResponse;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface AuthCandidateController {

    void registerCandidate(RequestCandidateRegistration requestRegistration);

    void loginCandidate(RequestCandidateLogin requestLogin, HttpSession httpSession);
}
