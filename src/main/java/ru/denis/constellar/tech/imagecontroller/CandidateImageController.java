package ru.denis.constellar.tech.imagecontroller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<byte[]> getCandidateAvatar(@PathVariable Long id, HttpSession session) throws IOException {

        if(session == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (Objects.isNull(candidate) || !candidate.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(candidate.getAvatarMimeType()));
        return new ResponseEntity<>(candidate.getAvatar(), headers, HttpStatus.OK);
    }

    @PostMapping("/candidate/upload-avatar")
    public void uploadCandidateAvatar(@RequestParam("avatarFile") MultipartFile file,
                                      HttpSession session, HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
            return;
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        candidate.setAvatar(file.getBytes());
        candidate.setAvatarMimeType(file.getContentType());
        candidateService.saveCandidate(candidate);


        response.sendRedirect("http://localhost:8080/candidate-personal-account");
    }


}
