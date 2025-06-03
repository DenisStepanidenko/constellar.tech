package ru.denis.constellar.tech.imagecontroller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;

import java.io.IOException;


@RestController
@RequestMapping("/image/employer")
@RequiredArgsConstructor
public class EmployerImageController {

    @Value("${ip}")
    private String ip;
    private final EmployerJpa employerJpa;

    @GetMapping("/getAvatar")
    public ResponseEntity<byte[]> getEmployerAvatar(HttpSession session) {

        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Employer employer = (Employer) session.getAttribute("employer");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(employer.getAvatarType()));
        return new ResponseEntity<>(employer.getAvatar(), headers, HttpStatus.OK);
    }

    @PostMapping("/upload-logo")
    public void uploadEmployerAvatar(@RequestParam("logoFile") MultipartFile file,
                                     HttpSession session, HttpServletResponse response) throws IOException {


        if (session == null || session.getAttribute("employer") == null) {
            response.sendRedirect("http://" + ip + ":8080/home");
            return;
        }

        Employer employer = (Employer) session.getAttribute("employer");

        employer.setAvatar(file.getBytes());
        employer.setAvatarType(file.getContentType());

        employerJpa.save(employer);
        employerJpa.flush();

        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());

        response.sendRedirect("http://" + ip + ":8080/company-personal-account-home");

    }
}
