package ru.denis.constellar.tech.achievement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.denis.constellar.tech.achievement.jpa.AchievementJpa;
import ru.denis.constellar.tech.achievement.model.Achievement;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementServiceUnitTest {

    @Mock
    private AchievementJpa achievementJpa;

    @InjectMocks
    private AchievementService achievementService;

    @Test
    void deleteByEntity_ValidAchievement_CallsDelete() {
        // GIVEN
        Achievement achievement = Achievement.builder()
                .id(1L)
                .title("Test Achievement")
                .date(LocalDate.now())
                .fileName("test.pdf")
                .fileType("application/pdf")
                .fileContent(new byte[]{1, 2, 3})
                .candidate(new Candidate())
                .build();

        // WHEN
        achievementService.deleteByEntity(achievement);

        // THEN
        verify(achievementJpa, times(1)).delete(achievement);
        verifyNoMoreInteractions(achievementJpa);
    }

    @Test
    void deleteByEntity_NullAchievement_DoesNotCallDelete() {
        // WHEN
        achievementService.deleteByEntity(null);

        // THEN
        verify(achievementJpa, never()).delete(any(Achievement.class));
    }
}