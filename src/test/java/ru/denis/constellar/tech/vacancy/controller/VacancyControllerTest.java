package ru.denis.constellar.tech.vacancy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.validation.BindingResult;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;
import ru.denis.constellar.tech.vacancy.dto.VacancyDetailsDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyStatsResponse;
import ru.denis.constellar.tech.vacancy.jpa.VacancyJpa;
import ru.denis.constellar.tech.vacancy.model.*;
import ru.denis.constellar.tech.vacancy.service.VacancyService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacancyControllerTest {

    @Mock
    private VacancyJpa vacancyJpa;
    @Mock
    private EmployerJpa employerJpa;
    @Mock
    private VacancyService vacancyService;
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private VacancyController vacancyController;

    private Employer testEmployer;
    private Candidate testCandidate;
    private Vacancy testVacancy;
    private VacancyDto testVacancyDto;
    private MockHttpSession session;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        testEmployer = new Employer();
        testEmployer.setId(1L);
        testEmployer.setCompanyName("Test Company");

        testCandidate = new Candidate();
        testCandidate.setId(1L);
        testCandidate.setUsername("testuser");

        testVacancy = new Vacancy();
        testVacancy.setId(1L);
        testVacancy.setTitle("Test Vacancy");
        testVacancy.setEmployer(testEmployer);
        testVacancy.setApplications(new HashSet<>());

        testVacancyDto = new VacancyDto();
        testVacancyDto.setTitle("New Vacancy");
        testVacancyDto.setDescription("Test Description");
        testVacancyDto.setSalaryFrom(100000);
        testVacancyDto.setSalaryTo(150000);

        session = new MockHttpSession();
        response = new MockHttpServletResponse();
    }

    @Test
    void createVacancy_ValidRequest_CreatesVacancyAndRedirects() throws IOException {
        // Arrange
        session.setAttribute("employer", testEmployer);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(employerJpa.findById(1L)).thenReturn(Optional.of(testEmployer));

        // Act
        vacancyController.createVacancy(testVacancyDto, bindingResult, session, response);

        // Assert
        verify(vacancyJpa, times(1)).save(any(Vacancy.class));
        assertEquals("http://localhost:8080/employer-list-vacancies", response.getRedirectedUrl());
    }

    @Test
    void createVacancy_Unauthorized_ThrowsException() {
        // Act & Assert
        assertThrows(UnauthorizedAccessException.class, () -> {
            vacancyController.createVacancy(testVacancyDto, bindingResult, null, response);
        });
    }

    @Test
    void createVacancy_ValidationErrors_RedirectsBack() throws IOException {
        // Arrange
        session.setAttribute("employer", testEmployer);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        vacancyController.createVacancy(testVacancyDto, bindingResult, session, response);

        // Assert
        assertEquals("http://localhost:8080/employer-add-vacancy", response.getRedirectedUrl());
    }

    @Test
    void getVacancyDetails_SetsSessionAndRedirects() throws IOException {
        // Arrange
        VacancyDetailsDto detailsDto = new VacancyDetailsDto();
        when(vacancyService.createVacancyDetailsDtoFromVacancy(1L)).thenReturn(detailsDto);

        // Act
        vacancyController.getVacancyDetails(1L, session, response, null);

        // Assert
        assertEquals(detailsDto, session.getAttribute("vacancy"));
        assertEquals("http://localhost:8080/employer-view-vacancy", response.getRedirectedUrl());
    }

    @Test
    void getVacancyStats_ReturnsStats() {
        // Arrange
        VacancyStatsResponse statsResponse = new VacancyStatsResponse();
        when(vacancyService.getVacancyStats(1L, 30)).thenReturn(statsResponse);

        // Act
        ResponseEntity<VacancyStatsResponse> response = vacancyController.getVacancyStats(1L, 30);

        // Assert
        assertEquals(statsResponse, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }



    @Test
    void updateVacancy_UpdatesAndRefreshesSession() {
        // Arrange
        session.setAttribute("employer", testEmployer);
        session.setAttribute("vacancy", new VacancyDetailsDto());
        VacancyDetailsDto updatedDto = new VacancyDetailsDto();
        when(vacancyService.createVacancyDetailsDtoFromVacancy(1L)).thenReturn(updatedDto);

        // Act
        vacancyController.updateVacancy(1L, testVacancyDto, session);

        // Assert
        verify(vacancyService, times(1)).updateVacancy(1L, testVacancyDto);
        assertEquals(updatedDto, session.getAttribute("vacancy"));
    }

    @Test
    void deleteVacancy_DeletesAndUpdatesSession() {
        // Arrange
        session.setAttribute("employer", testEmployer);
        session.setAttribute("vacancy", new VacancyDetailsDto());
        when(employerJpa.findById(1L)).thenReturn(Optional.of(testEmployer));

        // Act
        vacancyController.deleteVacancy(1L, session);

        // Assert
        verify(vacancyJpa, times(1)).deleteById(1L);
        assertNull(session.getAttribute("vacancy"));
    }

    @Test
    void applyVacancy_NewApplication_AddsApplication() {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        when(vacancyJpa.findById(1L)).thenReturn(Optional.of(testVacancy));

        // Act
        vacancyController.applyVacancy(1L, session);

        // Assert
        assertEquals(1, testVacancy.getApplications().size());
        verify(vacancyJpa, times(1)).save(testVacancy);
    }

    @Test
    void applyVacancy_ExistingApplication_DoesNotAdd() {
        // Arrange
        session.setAttribute("candidate", testCandidate);
        Application existingApp = new Application();
        existingApp.setCandidate(testCandidate);
        testVacancy.getApplications().add(existingApp);
        when(vacancyJpa.findById(1L)).thenReturn(Optional.of(testVacancy));

        // Act
        vacancyController.applyVacancy(1L, session);

        // Assert
        assertEquals(1, testVacancy.getApplications().size());
        verify(vacancyJpa, never()).save(testVacancy);
    }
}