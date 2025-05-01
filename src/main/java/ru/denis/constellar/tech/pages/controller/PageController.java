package ru.denis.constellar.tech.pages.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.denis.constellar.tech.vacancy.dto.VacancyDto;

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

    @GetMapping("/candidate-repository-list")
    public String getCandidateAllRepositories(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "candidate-repository-list";
    }

    @GetMapping("/candidate-repository-add")
    public String getCandidateAddRepository(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "add-candidate-repository";
    }

    @GetMapping("/candidate-repository-page")
    public String getCandidateRepository(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "candidate-repository";
    }

    @GetMapping("/candidate-achievements")
    public String getAchievements(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "achivements-list";
    }

    @GetMapping("/login-company")
    public String getLoginCompany() {

        return "login-company";
    }

    @GetMapping("/register-company")
    public String getRegisterCompany() {
        return "register-company";
    }

    @GetMapping("/company-personal-account-home")
    public String getCompanyPersonalAccountHome(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        return "company-personal-account-home";

    }

    @GetMapping("/edit-employer-profile")
    public String getCompanyEditProfile(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        return "edit-employer-profile";
    }

    @GetMapping("/company-change-avatar")
    public String getCompanyChangeAvatar(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        return "employer-change-avatar";
    }

    @GetMapping("/employer-list-vacancies")
    public String getCompanyListVacancies(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        return "employer-list-vacancies";
    }

    @GetMapping("/employer-add-vacancy")
    public String getCompanyAddVacancy(HttpSession session, Model model) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        model.addAttribute("vacancyDto", new VacancyDto());

        return "employer-add-vacancy";
    }

    @GetMapping("/employer-view-vacancy")
    public String getCompanyVacancy(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }


        return "employer-view-vacancy";
    }


}
