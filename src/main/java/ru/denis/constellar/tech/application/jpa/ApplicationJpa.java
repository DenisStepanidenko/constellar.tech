package ru.denis.constellar.tech.application.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.vacancy.model.Vacancy;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ApplicationJpa extends JpaRepository<Application, Long> {

    int countByVacancy(Vacancy vacancy);

    int countByVacancyAndStatus(Vacancy vacancy, ApplicationStatus status);

    int countByVacancyAndAppliedAtBetween(Vacancy vacancy, LocalDateTime start, LocalDateTime end);

    int countByVacancyId(Long vacancyId);

    int countByVacancyIdAndStatus(Long vacancyId, ApplicationStatus status);

    int countByVacancyIdAndAppliedAtBetween(Long vacancyId, LocalDateTime start, LocalDateTime end);

    List<Application> findByVacancyId(Long vacancyId);
}
