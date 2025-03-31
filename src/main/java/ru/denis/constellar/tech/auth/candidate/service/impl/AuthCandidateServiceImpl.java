package ru.denis.constellar.tech.auth.candidate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.candidate.service.CandidateService;

@Service
@RequiredArgsConstructor
public class AuthCandidateServiceImpl implements AuthCandidateService {

    private final CandidateService candidateService;


    @Override
    public void registerCandidate(RequestCandidateRegistration requestRegistration) {

        boolean isThisEmailAlreadyExists = candidateService.isExistsByEmail(requestRegistration.getEmail());

        if (isThisEmailAlreadyExists) {
            throw new EmailAlreadyExists("Аккаунт с данным email уже зарегистрирован.");
        }

        candidateService.register(requestRegistration);
    }


}
