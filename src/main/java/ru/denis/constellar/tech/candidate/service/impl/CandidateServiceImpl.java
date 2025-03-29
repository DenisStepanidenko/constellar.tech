package ru.denis.constellar.tech.candidate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;

    @Override
    public boolean isExistsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }

    @Override
    public void register(RequestCandidateRegistration requestRegistration) {

        //TODO: шифровать пароль перед отправкой
        Candidate candidate = Candidate.builder()
                .username(requestRegistration.getUsername())
                .password(requestRegistration.getPassword())
                .email(requestRegistration.getEmail())
                .build();

        candidateRepository.save(candidate);
    }

}
