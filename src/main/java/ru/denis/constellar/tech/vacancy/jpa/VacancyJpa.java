package ru.denis.constellar.tech.vacancy.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.vacancy.model.Vacancy;

@Repository
public interface VacancyJpa extends JpaRepository<Vacancy, Long> {

}
