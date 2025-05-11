//package ru.denis.constellar.tech.achievement.controller;
//
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.denis.constellar.tech.achievement.jpa.AchievementJpa;
//import ru.denis.constellar.tech.achievement.model.Achievement;
//import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
//import ru.denis.constellar.tech.candidate.model.Candidate;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Testcontainers
//@Transactional
//@Ignore
//class AchievementControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Container
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
//
//    @Autowired
//    private  CandidateRepository candidateRepository;
//
//    @Autowired
//    private AchievementJpa achievementJpa;
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//    }
//
//    private MockHttpSession session;
//
//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }
//
//    @Test
//    void addAndDownloadAchievement_IntegrationTest() throws Exception {
//        // 1. Создаем и сохраняем Candidate в БД
//        Candidate candidate = new Candidate();
//        candidate = candidateRepository.save(candidate); // Сохраняем кандидата
//
//        // 2. Создаем сессию с сохраненным кандидатом
//        session = new MockHttpSession();
//        session.setAttribute("candidate", candidate);
//
//        // 3. Создаем тестовый файл
//        MockMultipartFile file = new MockMultipartFile(
//                "file", "test.pdf", "application/pdf", "PDF content".getBytes()
//        );
//
//        // 4. Добавляем ачивку
//        mockMvc.perform(multipart("/constellar.tech/api/v1/achievement/add")
//                        .file(file)
//                        .param("title", "Test Achievement")
//                        .param("date", LocalDate.now().toString())
//                        .session(session))
//                .andExpect(status().is3xxRedirection());
//
//        // 5. Получаем ID сохраненного Achievement
//        Achievement savedAchievement = achievementJpa.findAll().get(0);
//
//        // 6. Скачиваем файл
//        mockMvc.perform(get("/constellar.tech/api/v1/achievement/download/" + savedAchievement.getId())
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Disposition",
//                        "attachment; filename*=UTF-8''test.pdf"));
//    }
//
//    @Test
//    void deleteAchievement_IntegrationTest() throws Exception {
//
//        // 1. Создаем и сохраняем Candidate
//        Candidate candidate = new Candidate();
//        candidate = candidateRepository.save(candidate);
//
//        // 2. Создаем и сохраняем Achievement
//        Achievement achievement = new Achievement();
//        achievement.setCandidate(candidate);
//        achievement = achievementJpa.save(achievement);
//
//        // 3. Добавляем Achievement в коллекцию кандидата
//        candidate.getAchievements().add(achievement);
//        candidateRepository.save(candidate);
//
//        // 4. Создаем сессию с сохраненным Candidate
//        session = new MockHttpSession();
//        session.setAttribute("candidate", candidate);
//
//        // 5. Выполняем удаление
//        mockMvc.perform(post("/constellar.tech/api/v1/achievement/delete/" + achievement.getId())
//                        .session(session))
//                .andExpect(status().isOk());
//
//        // 6. Проверяем, что Achievement действительно удален
//        assertFalse(achievementJpa.existsById(achievement.getId()));
//        assertFalse(candidateRepository.findById(candidate.getId()).get()
//                .getAchievements().contains(achievement));
//    }
//
//
//}