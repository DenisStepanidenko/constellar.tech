package ru.denis.constellar.tech.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.denis.constellar.tech.auth.candidate.dto.RequestCandidateRegistration;
import ru.denis.constellar.tech.auth.util.PasswordConfirmable;
import ru.denis.constellar.tech.auth.validation.annotations.PasswordMatches;

import java.util.Objects;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    private final String messageIfPasswordNotContainsConfirmPassword = "Введённые пароли должны совпадать.";

    @Override
    public boolean isValid(PasswordConfirmable candidateRegistration, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(candidateRegistration)) {
            return true;
        }

        if (!Objects.equals(candidateRegistration.getPassword(), candidateRegistration.getConfirmPassword())) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNotContainsConfirmPassword);

            return false;
        }


        return true;
    }

    private void createCustomMessage(ConstraintValidatorContext constraintValidatorContext, String message) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addPropertyNode("password").addConstraintViolation();
    }


}
