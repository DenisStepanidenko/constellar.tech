package ru.denis.constellar.tech.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.denis.constellar.tech.repository.model.Repository;

@org.springframework.stereotype.Repository
public interface RepositoryJpa extends JpaRepository<Repository, Long> {

}
