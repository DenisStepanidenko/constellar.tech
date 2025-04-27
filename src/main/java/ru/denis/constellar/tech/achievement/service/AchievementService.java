package ru.denis.constellar.tech.achievement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.denis.constellar.tech.achievement.jpa.AchievementJpa;
import ru.denis.constellar.tech.achievement.model.Achievement;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementJpa jpa;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteByEntity(Achievement achievement) {
        jpa.delete(achievement);
    }

}
