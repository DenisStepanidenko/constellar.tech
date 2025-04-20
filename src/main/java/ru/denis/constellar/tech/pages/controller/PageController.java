package ru.denis.constellar.tech.pages.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.util.Objects;

@Controller
//TODO: все страницы с представлениями перенести сюда
public class PageController {

    @GetMapping("/home")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/candidate-personal-account")
    public String getCandidatePersonalAccountPage(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }


        return "candidate-personal-account-home";
    }

    @GetMapping("/change-avatar-candidate")
    public String getChangeAvatarCandidate(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }


        return "candidate-change-avatar";
    }

    @GetMapping("/edit-profile-candidate")
    public String getEditProfileCandidate(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "edit-profile-candidate";
    }


}
