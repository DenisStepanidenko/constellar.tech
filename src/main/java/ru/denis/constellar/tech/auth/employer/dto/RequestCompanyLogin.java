package ru.denis.constellar.tech.auth.employer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestCompanyLogin {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
