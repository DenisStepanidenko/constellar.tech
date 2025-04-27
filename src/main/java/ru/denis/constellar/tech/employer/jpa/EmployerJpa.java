package ru.denis.constellar.tech.employer.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.employer.model.Employer;

import java.util.Optional;

@Repository
public interface EmployerJpa extends JpaRepository<Employer, Long> {

    Optional<Employer> findByEmail(String email);

    boolean existsByEmail(String email);

}
