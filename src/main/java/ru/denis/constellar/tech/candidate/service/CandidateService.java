package ru.denis.constellar.tech.candidate.service;

import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.candidate.dto.CandidateUpdateRequest;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.util.Optional;

public interface CandidateService {

    boolean isExistsByEmail(String email);

    void register(RequestCandidateRegistration requestRegistration);

    void saveCandidate(Candidate candidate);

    Optional<Candidate> findByEmail(String email);

    void updateCandidate(CandidateUpdateRequest candidateUpdateRequest, Candidate candidate);

}
