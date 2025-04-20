package ru.denis.constellar.tech.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateUpdateRequest {

    private String fullName;
    private String position;
    private String skills;
    private String experience;
    private String githubUsername;
    private String about;
    private String username;
}
