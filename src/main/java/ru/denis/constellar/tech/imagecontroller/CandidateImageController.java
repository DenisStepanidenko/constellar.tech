package ru.denis.constellar.tech.imagecontroller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class CandidateImageController {

    @Value("${basePathToCandidateFiles}")
    private String basePathToCandidateFiles;

    private final CandidateService candidateService;

    @GetMapping("/candidate/getAvatar/{id}")
    public ResponseEntity<Resource> getCandidateAvatar(@PathVariable Long id, HttpSession session) throws IOException {

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (Objects.isNull(candidate) || !candidate.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (Objects.isNull(candidate.getAvatarUrl()) || candidate.getAvatarUrl().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Path filePath = Paths.get(basePathToCandidateFiles)
                .resolve(candidate.getAvatarUrl())
                .normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String contentType = Files.probeContentType(filePath);

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @PostMapping("/candidate/upload-avatar")
    public void uploadCandidateAvatar(@RequestParam("avatarFile") MultipartFile file,
                                      HttpSession session, HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:/home");
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Выберите файл для загрузки.");
        }

        String contentType = file.getContentType();

        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Можно загружать только изображения.");
        }

        Path baseDir = Paths.get(basePathToCandidateFiles);

        if (!Files.exists(baseDir)) {
            Files.createDirectory(baseDir);
        }

        baseDir = baseDir.resolve(candidate.getEmail());

        if (!Files.exists(baseDir)) {
            Files.createDirectory(baseDir);
        }

        String nameOfNewFile = file.getOriginalFilename();

        Path pathToNewFile = baseDir.resolve(nameOfNewFile);


        if (Files.exists(pathToNewFile)) {
            Files.delete(pathToNewFile);
        }

        Files.copy(file.getInputStream(), pathToNewFile);

        candidate.setAvatarUrl(candidate.getEmail() + "/" + nameOfNewFile);
        candidateService.saveCandidate(candidate);


        response.sendRedirect("http://localhost:8080/candidate-personal-account");
    }


}
