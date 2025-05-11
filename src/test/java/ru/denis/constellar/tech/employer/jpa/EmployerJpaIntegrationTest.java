//package ru.denis.constellar.tech.employer.jpa;
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
//import ru.denis.constellar.tech.employer.model.Employer;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Testcontainers
//@SpringBootTest
//@Ignore
//class EmployerJpaIntegrationTest {
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
//    private EmployerJpa employerJpa;
//
//    @BeforeEach
//    void setUp() {
//        employerJpa.deleteAll();
//    }
//
//    @Test
//    void findByEmail_WhenEmployerExists_ReturnsEmployer() {
//        // GIVEN
//        Employer employer = createTestEmployer("test@example.com");
//        employerJpa.save(employer);
//
//        // WHEN
//        Optional<Employer> result = employerJpa.findByEmail("test@example.com");
//
//        // THEN
//        assertTrue(result.isPresent());
//        assertEquals("test@example.com", result.get().getEmail());
//        assertEquals("Test Company", result.get().getCompanyName());
//    }
//
//    @Test
//    void findByEmail_WhenEmployerNotExists_ReturnsEmpty() {
//        // WHEN
//        Optional<Employer> result = employerJpa.findByEmail("nonexistent@example.com");
//
//        // THEN
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void existsByEmail_WhenEmployerExists_ReturnsTrue() {
//        // GIVEN
//        Employer employer = createTestEmployer("exists@example.com");
//        employerJpa.save(employer);
//
//        // WHEN
//        boolean exists = employerJpa.existsByEmail("exists@example.com");
//
//        // THEN
//        assertTrue(exists);
//    }
//
//    @Test
//    void existsByEmail_WhenEmployerNotExists_ReturnsFalse() {
//        // WHEN
//        boolean exists = employerJpa.existsByEmail("nonexistent@example.com");
//
//        // THEN
//        assertFalse(exists);
//    }
//
//    @Test
//    void findByEmail_IsCaseSensitive() {
//        // GIVEN
//        Employer employer = createTestEmployer("CaseSensitive@example.com");
//        employerJpa.save(employer);
//
//        // WHEN
//        Optional<Employer> result = employerJpa.findByEmail("casesensitive@example.com");
//
//        // THEN
//        assertFalse(result.isPresent());
//    }
//
//    @Test
//    void save_NewEmployer_GeneratesId() {
//        // GIVEN
//        Employer newEmployer = createTestEmployer("new@example.com");
//
//        // WHEN
//        Employer saved = employerJpa.save(newEmployer);
//
//        // THEN
//        assertNotNull(saved.getId());
//        assertEquals("new@example.com", saved.getEmail());
//    }
//
//    @Test
//    void save_UpdateEmployer_UpdatesFields() {
//        // GIVEN
//        Employer employer = createTestEmployer("update@example.com");
//        Employer saved = employerJpa.save(employer);
//
//        // WHEN
//        saved.setCompanyName("Updated Company");
//        Employer updated = employerJpa.save(saved);
//
//        // THEN
//        assertEquals(saved.getId(), updated.getId());
//        assertEquals("Updated Company", updated.getCompanyName());
//    }
//
//    @Test
//    void findByEmail_WithMultipleEmployers_ReturnsCorrectOne() {
//        // GIVEN
//        employerJpa.save(createTestEmployer("first@example.com"));
//        employerJpa.save(createTestEmployer("second@example.com"));
//        employerJpa.save(createTestEmployer("target@example.com"));
//        employerJpa.save(createTestEmployer("third@example.com"));
//
//        // WHEN
//        Optional<Employer> result = employerJpa.findByEmail("target@example.com");
//
//        // THEN
//        assertTrue(result.isPresent());
//        assertEquals("target@example.com", result.get().getEmail());
//    }
//
//    private Employer createTestEmployer(String email) {
//        return Employer.builder()
//                .email(email)
//                .password("password")
//                .companyName("Test Company")
//                .companyDescription("Test Description")
//                .website("test.com")
//                .industry("IT")
//                .phoneNumber("1234567890")
//                .address("Test Address")
//                .inn("1234567890")
//                .kpp("987654321")
//                .contactPerson("John Doe")
//                .contactPosition("HR Manager")
//                .build();
//    }
//}