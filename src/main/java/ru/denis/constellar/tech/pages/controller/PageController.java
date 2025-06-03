package ru.denis.constellar.tech.pages.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.denis.constellar.tech.application.jpa.ApplicationJpa;
import ru.denis.constellar.tech.application.model.Application;
import ru.denis.constellar.tech.application.model.ApplicationStatus;
import ru.denis.constellar.tech.candidate.jpa.CandidateRepository;
import ru.denis.constellar.tech.candidate.model.Candidate;
import ru.denis.constellar.tech.employer.jpa.EmployerJpa;
import ru.denis.constellar.tech.employer.model.Employer;
import ru.denis.constellar.tech.vacancy.dto.VacancyDetailsDto;
import ru.denis.constellar.tech.vacancy.dto.VacancyDto;
import ru.denis.constellar.tech.vacancy.service.VacancyService;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
//TODO: все страницы с представлениями перенести сюда
public class PageController {

    private final VacancyService vacancyService;

    private final ApplicationJpa applicationJpa;

    private final CandidateRepository candidateRepository;
    private final EmployerJpa employerJpa;


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

        Employer employer = (Employer) session.getAttribute("employer");

        session.setAttribute("employer", employerJpa.findById(employer.getId()).get());

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

    @GetMapping("/employer-edit-vacancy")
    public String getCompanyEditVacancy(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }


        return "employer-edit-vacancy";
    }

    @GetMapping("/candidate-vacancies")
    public String getVacanciesForCandidate(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        session.setAttribute("vacancies", vacancyService.getAllVacancies());

        return "candidate-vacancies";
    }

    @GetMapping("/applications-for-employer")
    public String getAllApplicationsForEmployer(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        VacancyDetailsDto vacancyDetailsDto = (VacancyDetailsDto) session.getAttribute("vacancy");

        session.setAttribute("applications", applicationJpa.findByVacancyId(vacancyDetailsDto.getId()));

        return "applications-for-employer";
    }

    @GetMapping("/candidate-profile-for-employer/{applicationId}")
    public String getCandidateProfileForEmployer(HttpSession session, @PathVariable Long applicationId) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer")) || Objects.isNull(session.getAttribute("vacancy"))) {
            return "home";
        }


        Application application = applicationJpa.findById(applicationId).orElseThrow(RuntimeException::new);


        if (application.getStatus() == ApplicationStatus.NEW) {
            application.setStatus(ApplicationStatus.VIEWED);
            applicationJpa.save(application);
        }

        session.setAttribute("candidate", application.getCandidate());

        return "candidate-profile-for-employer-home";

    }

    @GetMapping("/candidate-profile-for-employer-2/{candidateId}")
    public String getCandidateProfileForEmployer2(HttpSession session, @PathVariable Long candidateId) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }


        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(RuntimeException::new);


        session.setAttribute("candidate", candidate);

        return "candidate-profile-for-employer-home-2";

    }

    @GetMapping("/candidate-profile-for-employer")
    public String getCandidateProfileEmployer2(HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            return "home";
        }

        return "candidate-profile-for-employer-home";
    }

    @GetMapping("/candidate-profile-for-employer-2")
    public String getCandidateProfileEmployer3(HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            return "home";
        }

        return "candidate-profile-for-employer-home-2";
    }


    @GetMapping("/candidate-profile-for-employer-repositories")
    public String getCandidateProfileRepositoriesForEmployer(HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            return "home";
        }


        return "candidate-profile-for-employer-repositories";
    }

    @GetMapping("/candidate-profile-for-employer-repositories-2")
    public String getCandidateProfileRepositoriesForEmployer3(HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            return "home";
        }


        return "candidate-profile-for-employer-repositories-2";
    }

    @GetMapping("/candidate-repository-page-for-employer-2")
    public String getCandidateProfileRepositoriesForEmployer2(HttpSession session) {

        if (session == null || session.getAttribute("candidate") == null) {
            return "home";
        }


        return "candidate-repository-page-for-employer-2";
    }


    @GetMapping("/candidate-repository-page-for-employer")
    public String getCandidateRepositoryPageForEmployer(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "candidate-repository-page-for-employer";
    }

    @GetMapping("/candidate-achievements-for-employer")
    public String getCandidateAchievementListForEmployer(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "candidate-achievements-list-for-employer";
    }

    @GetMapping("/candidate-achievements-for-employer-2")
    public String getCandidateAchievementListForEmployer2(HttpSession session) {
        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }

        return "candidate-achievements-list-for-employer-2";
    }


    @GetMapping("/candidate-applications-page")
    public String getCandidateApplicationsPage(HttpSession session) {


        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("candidate"))) {
            return "home";
        }
        return "candidate-applications";
    }

    @GetMapping("/employer-team-formation")
    public String getTeamFormationPage(HttpSession session) {

        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        return "employer-team-formation";
    }

    @GetMapping("/all-candidates")
    public String getAllCandidatesPages(HttpSession session, Model model) {


        if (Objects.isNull(session)) {
            return "home";
        }

        if (Objects.isNull(session.getAttribute("employer"))) {
            return "home";
        }

        List<Candidate> allCandidates = candidateRepository.findAll();
        model.addAttribute("candidates", allCandidates);
        return "all-candidates-page";
    }


}
