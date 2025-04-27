package ru.denis.constellar.tech.auth.employer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denis.constellar.tech.auth.util.PasswordConfirmable;
import ru.denis.constellar.tech.auth.validation.annotations.PasswordMatches;
import ru.denis.constellar.tech.auth.validation.annotations.ValidPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class CompanyRegistrationRequest implements PasswordConfirmable {
    @NotBlank
    @Size(min = 2, max = 50)
    private String companyName;

    @NotBlank
    private String inn;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    @NotBlank
    private String confirmPassword;
}
