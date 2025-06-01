package ru.denis.constellar.tech.team.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denis.constellar.tech.team.dto.TeamFormationResult;
import ru.denis.constellar.tech.team.dto.TeamRequest;
import ru.denis.constellar.tech.team.service.TeamFormationService;
import ru.denis.constellar.tech.vacancy.service.VacancyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {
    private final TeamFormationService teamFormationService;
    private final VacancyService vacancyService;


    @PostMapping("/form")
    public ResponseEntity<TeamFormationResult> formTeam(@RequestBody TeamRequest request) {
        TeamFormationResult result = teamFormationService.formTeam(request);
        return ResponseEntity.ok(result);
    }


}
