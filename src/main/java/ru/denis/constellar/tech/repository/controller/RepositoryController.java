package ru.denis.constellar.tech.repository.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;
import ru.denis.constellar.tech.repository.model.Repository;
import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/repository")
public class RepositoryController {

    private final CandidateService candidateService;

    @PostMapping("/add")
    public void addRepository(@RequestParam("name") String name,
                              @RequestParam("primaryLanguage") String primaryLanguage,
                              @RequestParam("description") String description,
                              @RequestParam("files") MultipartFile[] files,
                              HttpSession session,
                              HttpServletResponse response
    ) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        Repository repository = new Repository();
        repository.setName(name);
        repository.setDescription(description);
        repository.setPrimaryLanguage(primaryLanguage);
        repository.setLastUpdated(LocalDateTime.now());
        repository.setStars(0);
        repository.setCandidate(candidate);

        List<RepositoryFile> repositoryFileList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                RepositoryFile repositoryFile = new RepositoryFile();
                repositoryFile.setFileName(file.getOriginalFilename());
                repositoryFile.setContent(file.getBytes());
                repositoryFile.setRepository(repository);
                repositoryFileList.add(repositoryFile);
            }
        }
        repository.setFiles(repositoryFileList);

        candidate.getRepositories().add(repository);

        candidateService.saveCandidate(candidate);

        response.sendRedirect("http://localhost:8080/candidate-repository-list");
    }


}
