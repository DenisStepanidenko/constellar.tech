package ru.denis.constellar.tech.team.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.denis.constellar.tech.candidate.model.Candidate;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class TeamFormationResult {
    private List<Candidate> team;
    private Set<String> coveredSkills;
    private Set<String> missingSkills;
    private double teamScore;

}