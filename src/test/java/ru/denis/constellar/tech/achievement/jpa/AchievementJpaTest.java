//package ru.denis.constellar.tech.achievement.jpa;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.denis.constellar.tech.achievement.model.Achievement;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//@SpringBootTest
//@Ignore
//class AchievementJpaTest {
//
//    // Инициализация Testcontainers для PostgreSQL
//    @Container
//    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
//            .withDatabaseName("testdb")
//            .withUsername("test")
//            .withPassword("test");
//
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
//    }
//
//    @Autowired
//    private AchievementJpa achievementJpa;
//
//    @BeforeEach
//    void setUp() {
//        // Очистка базы перед каждым тестом
//        achievementJpa.deleteAll();
//    }
//
//    @Test
//    void save_ValidAchievement_SavesAndReturnsAchievement() {
//        // GIVEN
//        Achievement achievement = new Achievement();
//        achievement.setTitle("Test Achievement");
//        achievement.setDate(LocalDate.now());
//        achievement.setFileName("test.pdf");
//        achievement.setFileType("application/pdf");
//        achievement.setFileContent(new byte[]{1, 2, 3});
//
//        // WHEN
//        Achievement saved = achievementJpa.save(achievement);
//
//        // THEN
//        assertNotNull(saved.getId(), "ID должен быть сгенерирован");
//        assertEquals("Test Achievement", saved.getTitle());
//        assertEquals("test.pdf", saved.getFileName());
//        assertEquals("application/pdf", saved.getFileType());
//        assertArrayEquals(new byte[]{1, 2, 3}, saved.getFileContent());
//    }
//
//    @Test
//    void findById_ExistingId_ReturnsAchievement() {
//        // GIVEN
//        Achievement achievement = new Achievement();
//        achievement.setTitle("Test Achievement");
//        achievement.setDate(LocalDate.now());
//        Achievement saved = achievementJpa.save(achievement);
//
//        // WHEN
//        Optional<Achievement> found = achievementJpa.findById(saved.getId());
//
//        // THEN
//        assertTrue(found.isPresent(), "Достижение должно быть найдено");
//        assertEquals(saved.getId(), found.get().getId());
//        assertEquals("Test Achievement", found.get().getTitle());
//    }
//
//    @Test
//    void findById_NonExistingId_ReturnsEmpty() {
//        // WHEN
//        Optional<Achievement> found = achievementJpa.findById(999L);
//
//        // THEN
//        assertFalse(found.isPresent(), "Достижение не должно быть найдено");
//    }
//
//    @Test
//    void deleteById_ExistingId_DeletesAchievement() {
//        // GIVEN
//        Achievement achievement = new Achievement();
//        achievement.setTitle("Test Achievement");
//        Achievement saved = achievementJpa.save(achievement);
//        Long id = saved.getId();
//
//        // WHEN
//        achievementJpa.deleteById(id);
//
//        // THEN
//        Optional<Achievement> found = achievementJpa.findById(id);
//        assertFalse(found.isPresent(), "Достижение должно быть удалено");
//    }
//
//    @Test
//    void deleteById_NonExistingId_DoesNotThrowError() {
//        // WHEN + THEN
//        assertDoesNotThrow(() -> achievementJpa.deleteById(999L), "Удаление несуществующего ID не должно вызывать ошибку");
//    }
//}