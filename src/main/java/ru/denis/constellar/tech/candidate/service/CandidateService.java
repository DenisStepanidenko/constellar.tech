package ru.denis.constellar.tech.candidate.service;

import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.candidate.model.Candidate;

public interface CandidateService {

    boolean isExistsByEmail(String email);

    void register(RequestCandidateRegistration requestRegistration);

    void saveCandidate(Candidate candidate);
}
