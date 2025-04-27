package ru.denis.constellar.tech.achievement.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.achievement.jpa.AchievementJpa;
import ru.denis.constellar.tech.achievement.model.Achievement;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/achievement")
public class AchievementController {

    private final CandidateService candidateService;
    private final AchievementJpa achievementJpa;
    private final CandidateRepository candidateRepository;

    @PostMapping("/add")
    public void add(@RequestParam("title") String title,
                    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                    @RequestParam("file") MultipartFile file,
                    HttpSession session,
                    HttpServletResponse response) throws IOException {


        if (session == null || Objects.isNull(session.getAttribute("candidate"))) {
            throw new UnauthorizedAccessException();
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new RuntimeException();
        }

        Achievement achievement = new Achievement();
        achievement.setTitle(title);
        achievement.setDate(date);
        achievement.setFileName(file.getOriginalFilename());
        achievement.setFileType(contentType);
        achievement.setFileContent(file.getBytes());
        achievement.setCandidate(candidate);

        achievementJpa.save(achievement);

        session.setAttribute("candidate", candidateRepository.findById(candidate.getId()).get());

        response.sendRedirect("http://localhost:8080/candidate-achievements");
    }

    @GetMapping("/view/{achievementId}")
    public ResponseEntity<byte[]> view(@PathVariable Long achievementId,
                                       HttpSession session,
                                       HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            throw new UnauthorizedAccessException();
        }

        Achievement achievement = achievementJpa.findById(achievementId).orElseThrow(RuntimeException::new);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(achievement.getFileType()));
        return new ResponseEntity<>(achievement.getFileContent(), headers, HttpStatus.OK);


    }

    @GetMapping("/download/{achievementId}")
    public ResponseEntity<byte[]> download(@PathVariable Long achievementId,
                                           HttpSession session,
                                           HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            throw new UnauthorizedAccessException();
        }

        Achievement file = achievementJpa.findById(achievementId).orElseThrow(RuntimeException::new);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        headers.set("Content-Disposition",
                "attachment; filename*=UTF-8''" + encodedFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getFileContent());


    }

    @PostMapping("/delete/{achievementId}")
    @Transactional
    public void delete(@PathVariable Long achievementId,
                       HttpSession session,
                       HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            throw new UnauthorizedAccessException();
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        Achievement achievement = achievementJpa.findById(achievementId).orElseThrow(RuntimeException::new);

        candidate.getAchievements().remove(achievement);
        achievementJpa.deleteById(achievementId);
    }
}
