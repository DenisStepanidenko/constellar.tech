package ru.denis.constellar.tech.auth.candidate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.denis.constellar.tech.auth.validation.annotations.PasswordMatches;
import ru.denis.constellar.tech.auth.validation.annotations.ValidPassword;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RequestCandidateRegistration {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(max = 15, message = "Имя пользователя не может быть длиннее 15 символов")
    private String username;

    @Email(message = "Email не соответствует шаблону")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @ValidPassword
    private String password;

    @NotBlank(message = "Пароль не может быть пустым")
    private String confirmPassword;

}
