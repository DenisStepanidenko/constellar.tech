package ru.denis.constellar.tech.auth.candidate.controller;

import org.springframework.validation.BindingResult;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface AuthCandidateController {

    void registerCandidate(RequestCandidateRegistration requestRegistration, BindingResult bindingResult);
}
