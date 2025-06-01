package ru.denis.constellar.tech.vacancy.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.denis.constellar.tech.application.jpa.ApplicationJpa;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.vacancy.dto.VacancyDetailsDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyStatsResponse;
import ru.denis.constellar.tech.vacancy.jpa.VacancyJpa;
import ru.denis.constellar.tech.vacancy.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VacancyServiceTest {

    @Mock
    private ApplicationJpa applicationRepository;
    @Mock
    private VacancyJpa vacancyJpa;

    @InjectMocks
    private VacancyService vacancyService;

    @Test
    void createVacancyDetailsDtoFromVacancy_ValidVacancy_ReturnsCorrectDto() {
        // Arrange
        Vacancy vacancy = createTestVacancy();
        when(vacancyJpa.findById(1L)).thenReturn(Optional.of(vacancy));
        when(applicationRepository.countByVacancy(vacancy)).thenReturn(10);
        when(applicationRepository.countByVacancyAndStatus(vacancy, ApplicationStatus.NEW)).thenReturn(5);
        when(applicationRepository.countByVacancyAndStatus(vacancy, ApplicationStatus.VIEWED)).thenReturn(3);
        when(applicationRepository.countByVacancyAndStatus(vacancy, ApplicationStatus.REJECTED)).thenReturn(1);
        when(applicationRepository.countByVacancyAndStatus(vacancy, ApplicationStatus.INVITED)).thenReturn(1);

        // Act
        VacancyDetailsDto result = vacancyService.createVacancyDetailsDtoFromVacancy(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Test Vacancy", result.getTitle());
        assertEquals("Senior Developer", result.getPosition());
        assertEquals(100000, result.getSalaryFrom());
        assertEquals(150000, result.getSalaryTo());
        assertEquals("USD", result.getCurrency());
        assertEquals("SENIOR", result.getExperienceLevel());
        assertEquals("FULL_TIME", result.getEmploymentType());
        assertEquals("REMOTE", result.getWorkSchedule());
        assertEquals("Remote", result.getLocation());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Java, Spring", result.getSkills());
        assertTrue(result.isActive());

        // Verify application statistics
        assertEquals(10, result.getTotalResponses());
        assertEquals(5, result.getNewResponses());
        assertEquals(3, result.getInReviewResponses());
        assertEquals(1, result.getRejectedResponses());
        assertEquals(1, result.getAcceptedResponses());
    }

    @Test
    void getVacancyStats_ValidVacancy_ReturnsCorrectStats() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        when(applicationRepository.countByVacancyId(1L)).thenReturn(20);
        when(applicationRepository.countByVacancyIdAndStatus(1L, ApplicationStatus.NEW)).thenReturn(10);
        when(applicationRepository.countByVacancyIdAndStatus(1L, ApplicationStatus.VIEWED)).thenReturn(5);
        when(applicationRepository.countByVacancyIdAndStatus(1L, ApplicationStatus.REJECTED)).thenReturn(3);
        when(applicationRepository.countByVacancyIdAndStatus(1L, ApplicationStatus.INVITED)).thenReturn(2);
        when(applicationRepository.countByVacancyIdAndAppliedAtBetween(1L, startOfDay, endOfDay))
                .thenReturn(5);

        // Act
        VacancyStatsResponse result = vacancyService.getVacancyStats(1L, 1);

        // Assert
        assertEquals(20, result.getTotal());
        assertEquals(10, result.getNewCount());
        assertEquals(5, result.getViewed());
        assertEquals(3, result.getRejected());
        assertEquals(2, result.getInvited());

        // Verify chart data
        assertEquals(1, result.getChartData().size());
        assertTrue(result.getChartData().containsKey(today.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM"))));
        assertEquals(5, result.getChartData().values().iterator().next());
    }

    @Test
    void updateVacancy_ValidData_UpdatesVacancy() {
        // Arrange
        Vacancy vacancy = createTestVacancy();
        VacancyDto dto = createTestVacancyDto();
        when(vacancyJpa.findById(1L)).thenReturn(Optional.of(vacancy));

        // Act
        vacancyService.updateVacancy(1L, dto);

        // Assert
        verify(vacancyJpa, times(1)).save(vacancy);
        verify(vacancyJpa, times(1)).flush();

        assertEquals("Updated Title", vacancy.getTitle());
        assertEquals("Updated Position", vacancy.getPosition());
        assertEquals(120000, vacancy.getSalaryFrom());
        assertEquals(180000, vacancy.getSalaryTo());
        assertEquals("EUR", vacancy.getCurrency());
        assertEquals(ExperienceLevel.MIDDLE, vacancy.getExperienceLevel());
        assertEquals(EmploymentType.PART_TIME, vacancy.getEmploymentType());
        assertEquals(WorkSchedule.HYBRID, vacancy.getWorkSchedule());
        assertEquals("Office", vacancy.getLocation());
        assertEquals("Updated Description", vacancy.getDescription());
    }

    @Test
    void getAllVacancies_ReturnsOnlyActive() {
        // Arrange
        Vacancy activeVacancy = createTestVacancy();
        Vacancy inactiveVacancy = createTestVacancy();
        inactiveVacancy.setIsActive(false);

        when(vacancyJpa.findByIsActive(true)).thenReturn(List.of(activeVacancy));

        // Act
        List<Vacancy> result = vacancyService.getAllVacancies();

        // Assert
        assertEquals(1, result.size());
        assertEquals(activeVacancy, result.get(0));
    }

    private Vacancy createTestVacancy() {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(1L);
        vacancy.setTitle("Test Vacancy");
        vacancy.setPosition("Senior Developer");
        vacancy.setSalaryFrom(100000);
        vacancy.setSalaryTo(150000);
        vacancy.setCurrency("USD");
        vacancy.setExperienceLevel(ExperienceLevel.SENIOR);
        vacancy.setEmploymentType(EmploymentType.FULL_TIME);
        vacancy.setWorkSchedule(WorkSchedule.REMOTE);
        vacancy.setLocation("Remote");
        vacancy.setDescription("Test Description");
        vacancy.setSkills("Java, Spring");
        vacancy.setCreatedAt(LocalDateTime.now());
        vacancy.setIsActive(true);
        return vacancy;
    }

    private VacancyDto createTestVacancyDto() {
        VacancyDto dto = new VacancyDto();
        dto.setTitle("Updated Title");
        dto.setPosition("Updated Position");
        dto.setSalaryFrom(120000);
        dto.setSalaryTo(180000);
        dto.setCurrency("EUR");
        dto.setExperienceLevel(ExperienceLevel.MIDDLE);
        dto.setEmploymentType(EmploymentType.PART_TIME);
        dto.setWorkSchedule(WorkSchedule.HYBRID);
        dto.setLocation("Office");
        dto.setDescription("Updated Description");
        dto.setIsActive(true);
        return dto;
    }

    @Test
    void createVacancyDetailsDtoFromVacancy_NotFound_ThrowsException() {
        // Arrange
        when(vacancyJpa.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            vacancyService.createVacancyDetailsDtoFromVacancy(1L);
        });
    }

    @Test
    void getVacancyStats_MultipleDays_ReturnsCorrectChartData() {
        // Arrange
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        when(applicationRepository.countByVacancyId(1L)).thenReturn(30);
        when(applicationRepository.countByVacancyIdAndAppliedAtBetween(
                eq(1L),
                eq(yesterday.atStartOfDay()),
                eq(yesterday.plusDays(1).atStartOfDay()))
        ).thenReturn(10);
        when(applicationRepository.countByVacancyIdAndAppliedAtBetween(
                eq(1L),
                eq(today.atStartOfDay()),
                eq(today.plusDays(1).atStartOfDay()))
        ).thenReturn(20);

        // Act
        VacancyStatsResponse result = vacancyService.getVacancyStats(1L, 2);

        // Assert
        assertEquals(2, result.getChartData().size());
        assertTrue(result.getChartData().containsKey(yesterday.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM"))));
        assertTrue(result.getChartData().containsKey(today.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM"))));
        assertEquals(10, result.getChartData().get(yesterday.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM"))));
        assertEquals(20, result.getChartData().get(today.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM"))));
    }

    @Test
    void updateVacancy_NotFound_ThrowsException() {
        // Arrange
        VacancyDto dto = createTestVacancyDto();
        when(vacancyJpa.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            vacancyService.updateVacancy(1L, dto);
        });
    }
}