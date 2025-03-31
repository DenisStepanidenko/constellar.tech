package ru.denis.constellar.tech.auth.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCandidateLogin {

    private String password;
    private String email;

}
