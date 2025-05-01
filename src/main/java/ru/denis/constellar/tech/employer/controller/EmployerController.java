package ru.denis.constellar.tech.employer.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/employer")
public class EmployerController {

    private final EmployerJpa employerJpa;

    @PostMapping("/update")
    public void updateEmployer(@ModelAttribute Employer updateEmployer,
                               HttpSession session,
                               HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("employer") == null) {
            throw new UnauthorizedAccessException();
        }

        Employer employer = (Employer) session.getAttribute("employer");

        if (!updateEmployer.getAddress().isBlank()) {
            employer.setAddress(updateEmployer.getAddress());
        }

        if (!updateEmployer.getCompanyDescription().isBlank()) {
            employer.setCompanyDescription(updateEmployer.getCompanyDescription());
        }

        if (!updateEmployer.getCompanyName().isBlank()) {
            employer.setCompanyName(updateEmployer.getCompanyName());
        }

        if (!updateEmployer.getInn().isBlank()) {
            employer.setInn(updateEmployer.getInn());
        }

        if (!updateEmployer.getIndustry().isBlank()) {
            employer.setIndustry(updateEmployer.getIndustry());
        }

        if (!updateEmployer.getWebsite().isBlank()) {
            employer.setWebsite(updateEmployer.getWebsite());
        }

        if (!updateEmployer.getPhoneNumber().isBlank()) {
            employer.setPhoneNumber(updateEmployer.getPhoneNumber());
        }

        if (!updateEmployer.getContactPerson().isBlank()) {
            employer.setContactPerson(updateEmployer.getContactPerson());
        }

        if (!updateEmployer.getContactPosition().isBlank()) {
            employer.setContactPosition(updateEmployer.getContactPosition());
        }

        employerJpa.save(employer);
        employerJpa.flush();


        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());

        response.sendRedirect("http://localhost:8080/company-personal-account-home");
    }
}
