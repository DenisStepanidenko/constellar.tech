package ru.denis.constellar.tech.team.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TeamRequest {
    private Set<String> requiredSkills;
    private int teamSize;
    private Long vacancyId;

}
