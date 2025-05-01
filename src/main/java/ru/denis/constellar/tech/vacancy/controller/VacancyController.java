package ru.denis.constellar.tech.vacancy.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/vacancy")
public class VacancyController {

    private final VacancyJpa vacancyJpa;
    private final EmployerJpa employerJpa;

    private final VacancyService vacancyService;

    @PostMapping("/create")
    public void createVacancy(@Valid @ModelAttribute("vacancyDto") VacancyDto vacancyDto,
                              BindingResult result,
                              HttpSession session,
                              HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("employer") == null) {
            throw new UnauthorizedAccessException();
        }

        if (result.hasErrors()) {
            response.sendRedirect("http://localhost:8080/employer-add-vacancy");
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
        response.sendRedirect("http://localhost:8080/employer-list-vacancies");

    }

    @GetMapping("/{id}")
    public void getVacancyDetails(@PathVariable Long id,
                                  HttpSession session,
                                  HttpServletResponse response,
                                  Model model) throws IOException {

        VacancyDetailsDto vacancyDetailsDto = vacancyService.createVacancyDetailsDtoFromVacancy(id);

        session.setAttribute("vacancy", vacancyDetailsDto);

        response.sendRedirect("http://localhost:8080/employer-view-vacancy");
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<VacancyStatsResponse> getVacancyStats(
            @PathVariable Long id,
            @RequestParam(defaultValue = "30") int days) {

        VacancyStatsResponse response = vacancyService.getVacancyStats(id, days);
        return ResponseEntity.ok(response);
    }


}
