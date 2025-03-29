package ru.denis.constellar.tech.candidate.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.util.Optional;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    boolean existsByEmail(String email);
}
