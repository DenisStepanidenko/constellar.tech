//package ru.denis.constellar.tech.vacancy.controller;
//
//import org.junit.Ignore;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.mock.web.MockHttpSession;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.denis.constellar.tech.application.model.Application;
//import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
//import ru.denis.constellar.tech.candidate.model.Candidate;
//import ru.denis.constellar.tech.employer.model.Employer;
//import ru.denis.constellar.tech.vacancy.dto.VacancyDto;
//import ru.denis.constellar.tech.vacancy.dto.VacancyStatsResponse;
//import ru.denis.constellar.tech.vacancy.jpa.VacancyJpa;
//import ru.denis.constellar.tech.vacancy.model.Vacancy;
//import ru.denis.constellar.tech.vacancy.service.VacancyService;
//
//import java.time.LocalDateTime;
//import java.util.HashSet;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Ignore
//class VacancyControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private VacancyJpa vacancyJpa;
//    @MockBean
//    private VacancyService vacancyService;
//
//    private Employer testEmployer;
//    private Candidate testCandidate;
//    private Vacancy testVacancy;
//    private MockHttpSession session;
//
//    @BeforeEach
//    void setUp() {
//        testEmployer = new Employer();
//        testEmployer.setId(1L);
//        testEmployer.setCompanyName("Test Company");
//
//        testCandidate = new Candidate();
//        testCandidate.setId(1L);
//        testCandidate.setUsername("testuser");
//
//        testVacancy = new Vacancy();
//        testVacancy.setId(1L);
//        testVacancy.setTitle("Test Vacancy");
//        testVacancy.setEmployer(testEmployer);
//        testVacancy.setApplications(new HashSet<>());
//
//        session = new MockHttpSession();
//    }
//
//
//
//    @Test
//    void getVacancyStats_IntegrationTest() throws Exception {
//        // Arrange
//        when(vacancyService.getVacancyStats(1L, 30)).thenReturn(new VacancyStatsResponse());
//
//        // Act & Assert
//        mockMvc.perform(get("/constellar.tech/api/v1/vacancy/1/stats")
//                        .param("days", "30"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").exists());
//    }
//
//
//
//    @Test
//    void applyVacancy_IntegrationTest() throws Exception {
//        // Arrange
//        session.setAttribute("candidate", testCandidate);
//        when(vacancyJpa.findById(1L)).thenReturn(Optional.of(testVacancy));
//
//        // Act & Assert
//        mockMvc.perform(post("/constellar.tech/api/v1/vacancy/apply/1")
//                        .session(session))
//                .andExpect(status().isOk());
//    }
//}