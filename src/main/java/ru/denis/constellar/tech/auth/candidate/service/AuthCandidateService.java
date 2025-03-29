package ru.denis.constellar.tech.auth.candidate.service;

import org.springframework.validation.BindingResult;
import ru.denis.constellar.tech.auth.candidate.dto.RegistrationResponse;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface AuthCandidateService {

    RegistrationResponse registerCandidate(RequestCandidateRegistration requestRegistration);


}
