package ru.denis.constellar.tech.vacancy.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denis.constellar.tech.application.jpa.ApplicationJpa;
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
import ru.denis.constellar.tech.vacancy.model.Vacancy;
import ru.denis.constellar.tech.vacancy.service.VacancyService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/vacancy")
@Slf4j
public class VacancyController {

    @Value("${ip}")
    private String ip;
    private final VacancyJpa vacancyJpa;
    private final EmployerJpa employerJpa;

    private final VacancyService vacancyService;
    private final ApplicationJpa applicationJpa;

    @PostMapping("/create")
    public void createVacancy(@Valid @ModelAttribute("vacancyDto") VacancyDto vacancyDto,
                              BindingResult result,
                              HttpSession session,
                              HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("employer") == null) {
            throw new UnauthorizedAccessException();
        }

        if (result.hasErrors()) {
            response.sendRedirect("http://" + ip + ":8080/employer-add-vacancy");
            return;
        }

        Employer employer = (Employer) session.getAttribute("employer");

        //TODO:перенести в сервис
        Vacancy vacancy = new Vacancy();
        vacancy.setEmployer(employer);
        vacancy.setTitle(vacancyDto.getTitle());
        vacancy.setDescription(vacancyDto.getDescription());
        vacancy.setPosition(vacancyDto.getPosition());
        vacancy.setSalaryFrom(vacancyDto.getSalaryFrom());
        vacancy.setSalaryTo(vacancyDto.getSalaryTo());
        vacancy.setCurrency(vacancyDto.getCurrency());
        vacancy.setExperienceLevel(vacancyDto.getExperienceLevel());
        vacancy.setSkills(vacancyDto.getSkills());
        vacancy.setEmploymentType(vacancyDto.getEmploymentType());
        vacancy.setWorkSchedule(vacancyDto.getWorkSchedule());
        vacancy.setLocation(vacancyDto.getLocation());
        vacancy.setCreatedAt(LocalDateTime.now());
        vacancy.setIsActive(true);

        vacancyJpa.save(vacancy);
        vacancyJpa.flush();

        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());
        response.sendRedirect("http://" + ip + ":8080/employer-list-vacancies");

    }

    @GetMapping("/{id}")
    public void getVacancyDetails(@PathVariable Long id,
                                  HttpSession session,
                                  HttpServletResponse response,
                                  Model model) throws IOException {

        log.info("Обратились к вакансии");

        VacancyDetailsDto vacancyDetailsDto = vacancyService.createVacancyDetailsDtoFromVacancy(id);

        session.setAttribute("vacancy", vacancyDetailsDto);

        response.sendRedirect("http://" + ip + ":8080/employer-view-vacancy");
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<VacancyStatsResponse> getVacancyStats(
            @PathVariable Long id,
            @RequestParam(defaultValue = "30") int days) {

        VacancyStatsResponse response = vacancyService.getVacancyStats(id, days);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{vacancyId}/status")
    public void updateStatus(@PathVariable Long vacancyId, HttpSession session) {

        if (session == null || session.getAttribute("employer") == null) {
            throw new UnauthorizedAccessException();
        }

        Vacancy vacancy = vacancyJpa.findById(vacancyId).orElseThrow(RuntimeException::new);

        vacancy.setIsActive(!vacancy.getIsActive());
        Vacancy savedVacancy = vacancyJpa.save(vacancy);

        VacancyDetailsDto vacancyDetailsDto = (VacancyDetailsDto) session.getAttribute("vacancy");
        vacancyDetailsDto.setActive(savedVacancy.getIsActive());

        Employer employer = (Employer) session.getAttribute("employer");

        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());
    }

    @PutMapping("/{id}")
    public void updateVacancy(@PathVariable Long id,
                              @RequestBody VacancyDto vacancyDto,
                              HttpSession session) {

        if (session == null || session.getAttribute("employer") == null || session.getAttribute("vacancy") == null) {
            throw new UnauthorizedAccessException();
        }

        vacancyService.updateVacancy(id, vacancyDto);

        session.setAttribute("vacancy", vacancyService.createVacancyDetailsDtoFromVacancy(id));
    }

    @PostMapping("/delete-vacancy/{id}")
    @Transactional
    public void deleteVacancy(@PathVariable Long id, HttpSession session) {

        if (session == null || session.getAttribute("employer") == null || session.getAttribute("vacancy") == null) {
            throw new UnauthorizedAccessException();
        }

        Employer employer = (Employer) session.getAttribute("employer");

        Vacancy vacancy = vacancyJpa.findById(id).orElseThrow(RuntimeException::new);
        vacancy.getApplications().clear();  // Hibernate сам выполнит DELETE
        vacancyJpa.saveAndFlush(vacancy);
        vacancyJpa.deleteById(id);

        session.removeAttribute("vacancy");

        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());

    }

    @PostMapping("/apply/{vacancyId}")
    public void applyVacancy(@PathVariable Long vacancyId, HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            throw new UnauthorizedAccessException();
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        Vacancy vacancy = vacancyJpa.findById(vacancyId).orElseThrow(RuntimeException::new);

        Set<Application> applicationSet = vacancy.getApplications();

        boolean isNeedToApply = true;

        for (Application application : applicationSet) {

            if (application.getCandidate().equals(candidate)) {
                isNeedToApply = false;
                break;
            }
        }

        if (isNeedToApply) {
            Application application = new Application();
            application.setAppliedAt(LocalDateTime.now());
            application.setStatus(ApplicationStatus.NEW);
            application.setCandidate(candidate);
            application.setVacancy(vacancy);

            vacancy.getApplications().add(application);
            vacancyJpa.save(vacancy);
        }


    }


}
