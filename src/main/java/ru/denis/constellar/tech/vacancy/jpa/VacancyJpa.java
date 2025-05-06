package ru.denis.constellar.tech.vacancy.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.vacancy.model.Vacancy;

import java.util.List;

@Repository
public interface VacancyJpa extends JpaRepository<Vacancy, Long> {


    @Modifying
    @Query("delete from Vacancy v where v.id = :id")
    void deleteById(@Param("id") Long id);

    List<Vacancy> findByIsActive(boolean isActive);
}
