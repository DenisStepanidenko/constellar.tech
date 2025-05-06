package ru.denis.constellar.tech.repository.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.candidate.service.CandidateService;
import ru.denis.constellar.tech.repository.jpa.RepositoryJpa;
import ru.denis.constellar.tech.repository.model.Repository;
import ru.denis.constellar.tech.repositoryFile.jpa.RepositoryFileJpa;
import ru.denis.constellar.tech.repositoryFile.model.RepositoryFile;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/constellar.tech/api/v1/repository")
public class RepositoryController {

    private final CandidateService candidateService;
    private final RepositoryJpa repositoryJpa;
    private final RepositoryFileJpa repositoryFileJpa;
    private final CandidateRepository candidateRepository;

    @GetMapping("/{repoId}/files")
    public List<RepositoryFile> getAllRepositoryFiles(@PathVariable Long repoId) {

        Repository repository = repositoryJpa.findById(repoId).orElseThrow(RuntimeException::new);


        return repository.getFiles();

    }

    @GetMapping("/{repoId}/file/{fileId}/content")
    public ResponseEntity<byte[]> getFileContent(@PathVariable Long repoId,
                                                 @PathVariable Long fileId,
                                                 HttpSession session) {

        RepositoryFile file = repositoryFileJpa.findById(fileId).orElseThrow(RuntimeException::new);

        // Получаем содержимое файла
        byte[] content = file.getContent();
        String contentType = determineContentType(file.getFileType());

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(content);
    }

    private String determineContentType(String fileName) {
        // Определяем Content-Type на основе расширения файла
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".pdf")) return "application/pdf";
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "text/javascript";
        if (fileName.endsWith(".json")) return "application/json";
        return "text/plain";
    }


    @PostMapping("/add")
    @Transactional
    public void addRepository(@RequestParam("name") String name,
                              @RequestParam("primaryLanguage") String primaryLanguage,
                              @RequestParam("description") String description,
                              @RequestParam("files") MultipartFile[] files,
                              HttpSession session,
                              HttpServletResponse response
    ) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
            return;
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
                repositoryFile.setFileType(file.getContentType());
                repositoryFileList.add(repositoryFile);
            }
        }
        repository.setFiles(repositoryFileList);

        repositoryJpa.save(repository);

        session.setAttribute("candidate", candidateRepository.findById(candidate.getId()).get());

        response.sendRedirect("http://localhost:8080/candidate-repository-list");
    }

    @GetMapping("/forEmployer/{repositoryId}")
    public void getRepositoryForEmployer(@PathVariable Long repositoryId,
                                         @RequestParam(required = false) Long fileId,
                                         HttpSession session,
                                         HttpServletResponse response) throws IOException {


        session.removeAttribute("repository");
        session.removeAttribute("contentType");
        session.removeAttribute("fileContent");
        session.removeAttribute("selectedFile");

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        //TODO: создать своё собственное исключение
        Repository repository = repositoryJpa.findById(repositoryId)
                .orElseThrow(BadRequestException::new);


        session.setAttribute("repository", repository);

        if (fileId != null) {
            repository.getFiles().stream()
                    .filter(f -> f.getId().equals(fileId))
                    .findFirst()
                    .ifPresent(file -> {
                        session.setAttribute("selectedFile", file);

                        if (isTextFile(file)) {
                            session.setAttribute("contentType", "text");
                            session.setAttribute("fileContent", new String(file.getContent(), StandardCharsets.UTF_8));
                        } else if (isImageFile(file)) {
                            session.setAttribute("contentType", "image");
                            String base64 = Base64.getEncoder().encodeToString(file.getContent());
                            session.setAttribute("fileContent", "data:" + file.getFileType() + ";base64," + base64);
                        } else if (isPDFFile(file)) {
                            session.setAttribute("contentType", "pdf");
                            String base64 = Base64.getEncoder().encodeToString(file.getContent());
                            session.setAttribute("fileContent", base64);
                        } else {
                            session.setAttribute("contentType", "binary");
                        }


                    });
        }

        response.sendRedirect("http://localhost:8080/candidate-repository-page-for-employer");
    }

    @GetMapping("/{repositoryId}")
    @Transactional
    public void getRepository(@PathVariable Long repositoryId,
                              @RequestParam(required = false) Long fileId,
                              HttpSession session,
                              HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
        }

        session.removeAttribute("repository");
        session.removeAttribute("contentType");
        session.removeAttribute("fileContent");
        session.removeAttribute("selectedFile");


        Candidate candidate = (Candidate) session.getAttribute("candidate");

        //TODO: создать своё собственное исключение
        Repository repository = repositoryJpa.findById(repositoryId)
                .orElseThrow(BadRequestException::new);

        if (!repository.getCandidate().getId().equals(candidate.getId())) {
            throw new AccessDeniedException("Нет доступа к этому репозиторию.");
        }

        session.setAttribute("repository", repository);

        if (fileId != null) {
            repository.getFiles().stream()
                    .filter(f -> f.getId().equals(fileId))
                    .findFirst()
                    .ifPresent(file -> {
                        session.setAttribute("selectedFile", file);

                        if (isTextFile(file)) {
                            session.setAttribute("contentType", "text");
                            session.setAttribute("fileContent", new String(file.getContent(), StandardCharsets.UTF_8));
                        } else if (isImageFile(file)) {
                            session.setAttribute("contentType", "image");
                            String base64 = Base64.getEncoder().encodeToString(file.getContent());
                            session.setAttribute("fileContent", "data:" + file.getFileType() + ";base64," + base64);
                        } else if (isPDFFile(file)) {
                            session.setAttribute("contentType", "pdf");
                            String base64 = Base64.getEncoder().encodeToString(file.getContent());
                            session.setAttribute("fileContent", base64);
                        } else {
                            session.setAttribute("contentType", "binary");
                        }


                    });
        }

        response.sendRedirect("http://localhost:8080/candidate-repository-page");

    }

    @GetMapping("/viewpdf/{fileId}")
    @Transactional
    public void viewPdf(@PathVariable Long fileId,
                        HttpServletResponse response) throws IOException {
        RepositoryFile file = repositoryFileJpa.findById(fileId)
                .orElseThrow(RuntimeException::new);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getFileType()));

        // Устанавливаем Content-Type
        response.setContentType("application/pdf");

        // Кодируем имя файла в соответствии с RFC 5987
        String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // Устанавливаем заголовки с кодированным именем файла
        String contentDisposition = String.format(
                "inline; filename=\"%s\"; filename*=UTF-8''%s",
                file.getFileName(),
                encodedFileName
        );
        response.setHeader("Content-Disposition", contentDisposition);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("X-Frame-Options", "ALLOW-FROM http://localhost:8080");
        response.setHeader("Content-Security-Policy", "frame-ancestors 'self' http://localhost:8080;");

        // Отправляем содержимое файла
        try (OutputStream out = response.getOutputStream()) {
            out.write(file.getContent());
        }

    }

    @GetMapping("/download-file/{fileId}")
    @Transactional
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long fileId) throws IOException {
        RepositoryFile file = repositoryFileJpa.findById(fileId)
                .orElseThrow(() -> new RuntimeException());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);


        String encodedFileName = URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");


        headers.set("Content-Disposition",
                "attachment; filename=\"" + file.getFileName() + "\"; " +
                "filename*=UTF-8''" + encodedFileName);


        return ResponseEntity.ok()
                .headers(headers)
                .body(file.getContent());
    }


    @DeleteMapping("/{repositoryId}/file/{fileId}")
    @Transactional
    public ResponseEntity<?> deleteFile(@PathVariable Long repositoryId, @PathVariable Long fileId, HttpSession session, HttpServletResponse response) throws IOException {

        if (session == null || session.getAttribute("candidate") == null) {
            response.sendRedirect("http://localhost:8080/home");
        }

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        Repository repository = repositoryJpa.findById(repositoryId).orElseThrow(RuntimeException::new);

        if (!repository.getCandidate().getId().equals(candidate.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Нет доступа к репозиторию");
        }

        repository.getFiles().removeIf(file -> file.getId().equals(fileId));


        candidate.getRepositories().stream().filter(repo -> repo.getId().equals(repositoryId)).map(repo -> repo.getFiles().removeIf(file -> file.getId().equals(fileId)));

        repositoryFileJpa.deleteById(fileId);

        repositoryJpa.save(repository);


        session.setAttribute("repository", repository);
        session.removeAttribute("contentType");
        session.removeAttribute("fileContent");
        session.removeAttribute("selectedFile");

        return ResponseEntity.ok().build();

    }


    private boolean isTextFile(RepositoryFile file) {
        return file.getFileType().startsWith("text/") ||
               file.getFileName().matches(".*\\.(java|cpp|py|js|html|css|xml|json|kt|ts|md|txt)$");
    }

    private boolean isImageFile(RepositoryFile file) {
        return file.getFileType().startsWith("image/") ||
               file.getFileName().matches(".*\\.(png|jpg|jpeg|gif|svg|bmp)$");
    }

    private boolean isPDFFile(RepositoryFile file) {
        return file.getFileType().equals("application/pdf") ||
               file.getFileName().endsWith(".pdf");
    }


}
