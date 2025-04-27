package ru.denis.constellar.tech.auth.candidate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class RequestCandidateRegistration implements PasswordConfirmable {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    @Size(min = 2, max = 15, message = "Длина имени пользователя может от 2-ух до 15-ти символов.")
    private String username;

    @Email(message = "Email не соответствует шаблону")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @ValidPassword
    private String password;

    @NotBlank(message = "Пароль не может быть пустым")
    private String confirmPassword;

}
