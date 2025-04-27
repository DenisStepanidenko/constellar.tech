package ru.denis.constellar.tech.repositoryFile.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;

@Repository
public interface RepositoryFileJpa extends JpaRepository<RepositoryFile, Long> {
}
