package ru.denis.constellar.tech.candidate.service;

import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;

public interface CandidateService {

    boolean isExistsByEmail(String email);

    void register(RequestCandidateRegistration requestRegistration);
}
