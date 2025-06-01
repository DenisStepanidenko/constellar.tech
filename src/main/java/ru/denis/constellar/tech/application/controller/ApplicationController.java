package ru.denis.constellar.tech.application.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.denis.constellar.tech.application.jpa.ApplicationJpa;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationJpa applicationJpa;


    @GetMapping("/candidate-applications")
    public void getApplicationsByCandidate(HttpSession session, HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            throw new UnauthorizedAccessException();
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        List<Application> applications = applicationJpa.findByCandidateId(candidate.getId());
        session.setAttribute("applications", applications);

        response.sendRedirect("http://localhost:8080/candidate-applications-page");
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(request.get("status"));


            Application application = applicationJpa.findById(id).orElseThrow(RuntimeException::new);
            application.setStatus(newStatus);
            applicationJpa.save(application);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Неверный статус");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка при обновлении статуса");
        }
    }
}
