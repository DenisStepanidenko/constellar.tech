package ru.denis.constellar.tech.auth.candidate.service.impl;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateLogin;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCandidateServiceImpl implements AuthCandidateService {

    private final CandidateService candidateService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerCandidate(RequestCandidateRegistration requestRegistration) {

        boolean isThisEmailAlreadyExists = candidateService.isExistsByEmail(requestRegistration.getEmail());

        if (isThisEmailAlreadyExists) {
            throw new EmailAlreadyExists("Аккаунт с данным email уже зарегистрирован.");
        }

        candidateService.register(requestRegistration);
    }

    @Override
    public void loginCandidate(RequestCandidateLogin requestLogin, HttpSession httpSession) {

        Candidate candidate = candidateService.findByEmail(requestLogin.getEmail()).orElseThrow(() -> new PasswordOrEmailIncorrect("Пароль или почта введены неверно."));

        String encodedPassword = candidate.getPassword();

        if (!passwordEncoder.matches(requestLogin.getPassword(), encodedPassword)) {
            throw new PasswordOrEmailIncorrect("Пароль или почта введены неверно.");
        }

        log.info("Пользователь с почтой " + requestLogin.getEmail() + " успешно вошёл");
        httpSession.setAttribute("candidate", candidate);
    }


}
