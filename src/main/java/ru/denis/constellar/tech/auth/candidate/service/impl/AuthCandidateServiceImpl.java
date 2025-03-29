package ru.denis.constellar.tech.auth.candidate.service.impl;

import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.candidate.dto.RegistrationResponse;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.candidate.service.AuthCandidateService;
import ru.denis.constellar.tech.auth.exceptions.PasswordConfirmNotEqualsPassword;

@Service
public class AuthCandidateServiceImpl implements AuthCandidateService {


    @Override
    public RegistrationResponse registerCandidate(RequestCandidateRegistration requestRegistration) {

        return null;
    }



}
