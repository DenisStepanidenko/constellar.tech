package ru.denis.constellar.tech.achievement.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.denis.constellar.tech.achievement.model.Achievement;

@Repository
public interface AchievementJpa extends JpaRepository<Achievement, Long> {

    @Modifying
    @Query("delete from Achievement ach where ach.id = :id")
    void deleteById(@Param("id") Long id);
}
