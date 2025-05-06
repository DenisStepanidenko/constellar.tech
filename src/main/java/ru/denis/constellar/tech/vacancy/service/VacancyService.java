package ru.denis.constellar.tech.vacancy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denis.constellar.tech.application.jpa.ApplicationJpa;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.vacancy.dto.VacancyDetailsDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyStatsResponse;
import ru.denis.constellar.tech.vacancy.jpa.VacancyJpa;
import ru.denis.constellar.tech.vacancy.model.EmploymentType;
import ru.denis.constellar.tech.vacancy.model.ExperienceLevel;
import ru.denis.constellar.tech.vacancy.model.Vacancy;
import ru.denis.constellar.tech.vacancy.model.WorkSchedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class VacancyService {

    private final ApplicationJpa applicationRepository;


    private final VacancyJpa vacancyJpa;

    public VacancyDetailsDto createVacancyDetailsDtoFromVacancy(Long id) {

        VacancyDetailsDto dto = new VacancyDetailsDto();

        Vacancy vacancy = vacancyJpa.findById(id).orElseThrow(RuntimeException::new);

        dto.setId(vacancy.getId());
        dto.setTitle(vacancy.getTitle());
        dto.setPosition(vacancy.getPosition());
        dto.setSalaryFrom(vacancy.getSalaryFrom());
        dto.setSalaryTo(vacancy.getSalaryTo());
        dto.setCurrency(vacancy.getCurrency());
        dto.setExperienceLevel(vacancy.getExperienceLevel().name());
        dto.setEmploymentType(vacancy.getEmploymentType().name());
        dto.setWorkSchedule(vacancy.getWorkSchedule().name());
        dto.setLocation(vacancy.getLocation());
        dto.setDescription(vacancy.getDescription());

        dto.setSkills(vacancy.getSkills());
        dto.setCreatedAt(vacancy.getCreatedAt());
        dto.setActive(vacancy.getIsActive());

        setApplicationStatistics(vacancy, dto);


        return dto;
    }

    private void setApplicationStatistics(Vacancy vacancy, VacancyDetailsDto dto) {
        // Общее количество откликов
        int totalApplications = applicationRepository.countByVacancy(vacancy);
        dto.setTotalResponses(totalApplications);

        // Отклики по статусам
        dto.setNewResponses(applicationRepository.countByVacancyAndStatus(
                vacancy, ApplicationStatus.NEW));
        dto.setInReviewResponses(applicationRepository.countByVacancyAndStatus(
                vacancy, ApplicationStatus.VIEWED));
        dto.setRejectedResponses(applicationRepository.countByVacancyAndStatus(
                vacancy, ApplicationStatus.REJECTED));
        dto.setAcceptedResponses(applicationRepository.countByVacancyAndStatus(
                vacancy, ApplicationStatus.INVITED));
    }


    public VacancyStatsResponse getVacancyStats(Long vacancyId, int days) {
        VacancyStatsResponse response = new VacancyStatsResponse();

        // Основная статистика
        response.setTotal(applicationRepository.countByVacancyId(vacancyId));
        response.setNewCount(applicationRepository.countByVacancyIdAndStatus(vacancyId, ApplicationStatus.NEW));
        response.setViewed(applicationRepository.countByVacancyIdAndStatus(vacancyId, ApplicationStatus.VIEWED));
        response.setRejected(applicationRepository.countByVacancyIdAndStatus(vacancyId, ApplicationStatus.REJECTED));
        response.setInvited(applicationRepository.countByVacancyIdAndStatus(vacancyId, ApplicationStatus.INVITED));

        // Данные для графика
        Map<String, Integer> chartData = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = now.minusDays(i);
            String dateStr = date.format(formatter);
            int count = applicationRepository.countByVacancyIdAndAppliedAtBetween(
                    vacancyId,
                    date.atStartOfDay(),
                    date.plusDays(1).atStartOfDay()
            );
            chartData.put(dateStr, count);
        }

        response.setChartData(chartData);

        return response;
    }

    public void updateVacancy(Long id, VacancyDto vacancyDto) {
        Vacancy vacancy = vacancyJpa.findById(id).orElseThrow(RuntimeException::new);

        vacancy.setTitle(vacancyDto.getTitle());
        vacancy.setPosition(vacancyDto.getPosition());
        vacancy.setSalaryFrom(vacancyDto.getSalaryFrom());
        vacancy.setSalaryTo(vacancyDto.getSalaryTo());
        vacancy.setCurrency(vacancyDto.getCurrency());
        vacancy.setExperienceLevel(ExperienceLevel.valueOf(vacancyDto.getExperienceLevel().name()));
        vacancy.setEmploymentType(EmploymentType.valueOf(vacancyDto.getEmploymentType().name()));
        vacancy.setWorkSchedule(WorkSchedule.valueOf(vacancyDto.getWorkSchedule().name()));
        vacancy.setLocation(vacancyDto.getLocation());
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setIsActive(vacancyDto.getIsActive());
        vacancy.setSkills(vacancy.getSkills());

        vacancyJpa.save(vacancy);
        vacancyJpa.flush();

    }

    public List<Vacancy> getAllVacancies() {

        return vacancyJpa.findByIsActive(true);

    }
}
