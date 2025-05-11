//package ru.denis.constellar.tech.application.jpa;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import ru.denis.constellar.tech.application.model.Application;
//import ru.denis.constellar.tech.application.model.ApplicationStatus;
//import ru.denis.constellar.tech.candidate.model.Candidate;
//import ru.denis.constellar.tech.vacancy.model.EmploymentType;
//import ru.denis.constellar.tech.vacancy.model.ExperienceLevel;
//import ru.denis.constellar.tech.vacancy.model.Vacancy;
//import ru.denis.constellar.tech.vacancy.model.WorkSchedule;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//@SpringBootTest
//@Ignore
//class ApplicationJpaTest {
//
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
//    private ApplicationJpa applicationJpa;
//
//    @Autowired
//    private JpaRepository<Vacancy, Long> vacancyJpa;
//
//    @Autowired
//    private JpaRepository<Candidate, Long> candidateJpa;
//
//    private Vacancy vacancy;
//    private Candidate candidate;
//
//    @BeforeEach
//    void setUp() {
//        // Очистка базы перед каждым тестом
//        applicationJpa.deleteAll();
//        vacancyJpa.deleteAll();
//        candidateJpa.deleteAll();
//
//        // Создание тестового Candidate
//        candidate = new Candidate();
//        candidate.setUsername("testuser");
//        candidate.setEmail("test@example.com");
//        candidate.setPassword("password123");
//        candidate.setFullName("Test User");
//        candidate = candidateJpa.save(candidate);
//
//        // Создание тестового Vacancy
//        vacancy = new Vacancy();
//        vacancy.setTitle("Java Developer");
//        vacancy.setDescription("Develop awesome apps");
//        vacancy.setPosition("Senior Developer");
//        vacancy.setSalaryFrom(100000);
//        vacancy.setSalaryTo(150000);
//        vacancy.setCurrency("USD");
//        vacancy.setExperienceLevel(ExperienceLevel.SENIOR);
//        vacancy.setSkills("Java, Spring");
//        vacancy.setEmploymentType(EmploymentType.FULL_TIME);
//        vacancy.setWorkSchedule(WorkSchedule.REMOTE);
//        vacancy.setLocation("Remote");
//        vacancy.setCreatedAt(LocalDateTime.now());
//        vacancy.setIsActive(true);
//        vacancy = vacancyJpa.save(vacancy);
//    }
//
//    @Test
//    void save_ValidApplication_SavesAndReturnsApplication() {
//        // GIVEN
//        Application application = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//
//        // WHEN
//        Application saved = applicationJpa.save(application);
//
//        // THEN
//        assertNotNull(saved.getId(), "ID должен быть сгенерирован");
//        assertEquals(vacancy.getId(), saved.getVacancy().getId());
//        assertEquals(candidate.getId(), saved.getCandidate().getId());
//        assertEquals(ApplicationStatus.NEW, saved.getStatus());
//        assertNotNull(saved.getAppliedAt());
//    }
//
//    @Test
//    void findById_ExistingId_ReturnsApplication() {
//        // GIVEN
//        Application application = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        Application saved = applicationJpa.save(application);
//
//        // WHEN
//        Optional<Application> found = applicationJpa.findById(saved.getId());
//
//        // THEN
//        assertTrue(found.isPresent(), "Заявка должна быть найдена");
//        assertEquals(saved.getId(), found.get().getId());
//        assertEquals(ApplicationStatus.NEW, found.get().getStatus());
//        assertEquals(vacancy.getId(), found.get().getVacancy().getId());
//    }
//
//    @Test
//    void countByVacancy_ValidVacancy_ReturnsCorrectCount() {
//        // GIVEN
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        Application app2 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.VIEWED)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        applicationJpa.saveAll(List.of(app1, app2));
//
//        // WHEN
//        int count = applicationJpa.countByVacancy(vacancy);
//
//        // THEN
//        assertEquals(2, count, "Должно быть 2 заявки для вакансии");
//    }
//
//    @Test
//    void countByVacancyAndStatus_ValidVacancyAndStatus_ReturnsCorrectCount() {
//        // GIVEN
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        Application app2 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.VIEWED)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        applicationJpa.saveAll(List.of(app1, app2));
//
//        // WHEN
//        int count = applicationJpa.countByVacancyAndStatus(vacancy, ApplicationStatus.NEW);
//
//        // THEN
//        assertEquals(1, count, "Должна быть 1 заявка со статусом NEW");
//    }
//
//    @Test
//    void countByVacancyAndAppliedAtBetween_ValidRange_ReturnsCorrectCount() {
//        // GIVEN
//        LocalDateTime now = LocalDateTime.now();
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(now.minusDays(1))
//                .build();
//        Application app2 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(now.plusDays(1))
//                .build();
//        applicationJpa.saveAll(List.of(app1, app2));
//
//        // WHEN
//        int count = applicationJpa.countByVacancyAndAppliedAtBetween(vacancy, now.minusDays(2), now);
//
//        // THEN
//        assertEquals(1, count, "Должна быть 1 заявка в заданном диапазоне дат");
//    }
//
//    @Test
//    void countByVacancyId_ValidVacancyId_ReturnsCorrectCount() {
//        // GIVEN
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        applicationJpa.save(app1);
//
//        // WHEN
//        int count = applicationJpa.countByVacancyId(vacancy.getId());
//
//        // THEN
//        assertEquals(1, count, "Должна быть 1 заявка для vacancyId");
//    }
//
//    @Test
//    void countByVacancyIdAndStatus_ValidVacancyIdAndStatus_ReturnsCorrectCount() {
//        // GIVEN
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.REJECTED)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        applicationJpa.save(app1);
//
//        // WHEN
//        int count = applicationJpa.countByVacancyIdAndStatus(vacancy.getId(), ApplicationStatus.REJECTED);
//
//        // THEN
//        assertEquals(1, count, "Должна быть 1 заявка со статусом REJECTED");
//    }
//
//    @Test
//    void countByVacancyIdAndAppliedAtBetween_ValidRange_ReturnsCorrectCount() {
//        // GIVEN
//        LocalDateTime now = LocalDateTime.now();
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(now)
//                .build();
//        applicationJpa.save(app1);
//
//        // WHEN
//        int count = applicationJpa.countByVacancyIdAndAppliedAtBetween(vacancy.getId(), now.minusHours(1), now.plusHours(1));
//
//        // THEN
//        assertEquals(1, count, "Должна быть 1 заявка в диапазоне дат");
//    }
//
//    @Test
//    void findByVacancyId_ValidVacancyId_ReturnsApplications() {
//        // GIVEN
//        Application app1 = Application.builder()
//                .vacancy(vacancy)
//                .candidate(candidate)
//                .status(ApplicationStatus.NEW)
//                .appliedAt(LocalDateTime.now())
//                .build();
//        applicationJpa.save(app1);
//
//        // WHEN
//        List<Application> applications = applicationJpa.findByVacancyId(vacancy.getId());
//
//        // THEN
//        assertEquals(1, applications.size(), "Должна быть 1 заявка");
//        assertEquals(app1.getId(), applications.get(0).getId());
//        assertEquals(ApplicationStatus.NEW, applications.get(0).getStatus());
//        assertEquals(vacancy.getId(), applications.get(0).getVacancy().getId());
//    }
//}