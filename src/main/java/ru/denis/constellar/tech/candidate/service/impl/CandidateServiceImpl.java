package ru.denis.constellar.tech.candidate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }

    @Override
    public void register(RequestCandidateRegistration requestRegistration) {

        Candidate candidate = Candidate.builder()
                .username(requestRegistration.getUsername())
                .password(passwordEncoder.encode(requestRegistration.getPassword()))
                .email(requestRegistration.getEmail())
                .build();

        saveCandidate(candidate);
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> findByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }


}
