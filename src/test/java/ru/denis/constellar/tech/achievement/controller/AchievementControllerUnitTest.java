package ru.denis.constellar.tech.achievement.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import ru.denis.constellar.tech.achievement.jpa.AchievementJpa;
import ru.denis.constellar.tech.achievement.model.Achievement;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AchievementControllerUnitTest {

    @Mock private CandidateService candidateService;
    @Mock private AchievementJpa achievementJpa;
    @Mock private CandidateRepository candidateRepository;
    @Mock private HttpSession session;
    @Mock private HttpServletResponse response;

    @InjectMocks
    private AchievementController achievementController;


    @Test
    void add_ValidInput_SavesAchievement() throws IOException {
        Candidate candidate = new Candidate();
        when(session.getAttribute("candidate")).thenReturn(candidate);
        when(candidateRepository.findById(any())).thenReturn(Optional.of(candidate));

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "content".getBytes()
        );

        achievementController.add("Title", LocalDate.now(), file, session, response);

        verify(achievementJpa, times(1)).save(any(Achievement.class));
    }


    @Test
    void view_AchievementNotFound_ThrowsException() {
        when(session.getAttribute("candidate")).thenReturn(new Candidate());
        when(achievementJpa.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                achievementController.view(1L, session, response)
        );
    }


    @Test
    void download_ValidFile_ReturnsAttachment() throws IOException {
        Achievement achievement = new Achievement();
        achievement.setFileName("test.pdf");
        achievement.setFileType("application/pdf");
        achievement.setFileContent(new byte[]{1, 2, 3});

        when(session.getAttribute("candidate")).thenReturn(new Candidate());
        when(achievementJpa.findById(1L)).thenReturn(Optional.of(achievement));

        ResponseEntity<byte[]> responseEntity = achievementController.download(1L, session, response);

        assertEquals("attachment; filename*=UTF-8''test.pdf",
                responseEntity.getHeaders().getFirst("Content-Disposition"));
    }


    @Test
    void delete_ValidId_RemovesAchievement() throws IOException {
        Candidate candidate = new Candidate();
        Achievement achievement = new Achievement();
        candidate.getAchievements().add(achievement);

        when(session.getAttribute("candidate")).thenReturn(candidate);
        when(achievementJpa.findById(1L)).thenReturn(Optional.of(achievement));

        achievementController.delete(1L, session, response);

        assertFalse(candidate.getAchievements().contains(achievement));
        verify(achievementJpa, times(1)).deleteById(1L);
    }
}