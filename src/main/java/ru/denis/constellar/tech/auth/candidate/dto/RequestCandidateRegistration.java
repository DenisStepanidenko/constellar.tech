package ru.denis.constellar.tech.auth.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCandidateRegistration {

    private String username;
    private String email;
    private String password;

}
