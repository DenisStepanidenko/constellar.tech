package ru.denis.constellar.tech.candidate.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.denis.constellar.tech.candidate.dto.CandidateUpdateRequest;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/candidate")
public class CandidateController {

    @Value("${ip}")
    private String ip;
    private final CandidateService candidateService;
    private final CandidateRepository candidateRepository;

    @PostMapping("/update-candidate")
    public void updateCandidate(@RequestBody CandidateUpdateRequest candidateUpdateRequest, HttpSession session, HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
        }

        candidateService.updateCandidate(candidateUpdateRequest, (Candidate) session.getAttribute("candidate"));

        response.sendRedirect("http://" + ip + ":8080/candidate-personal-account");
    }


}
