package ru.denis.constellar.tech.auth.candidate.service;

import org.springframework.validation.BindingResult;
import ru.denis.constellar.tech.auth.candidate.dto.RegistrationResponse;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.candidate.model.Candidate;

public interface AuthCandidateService {

    void registerCandidate(RequestCandidateRegistration requestRegistration);

}
