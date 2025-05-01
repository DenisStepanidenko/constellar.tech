package ru.denis.constellar.tech.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import ru.denis.constellar.tech.auth.validation.annotations.ValidPassword;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    private final String messageIfPasswordNull = "Пароль не должен равняться null.";

    private final String messageIfPasswordNotContainsNumbers = "Пароль должен содержать цифры.";

    private final String messageIfPasswordNotContainsUppercaseLetters = "Пароль должен содержать заглавные буквы.";

    private final String messageIfPasswordNotContainsLowercaseLetters = "Пароль должен содержать строчные буквы.";

    private final String messageIfPasswordLengthLessThen8 = "Пароль должен содержать минимум 8 символов.";

    private final String messageIfPasswordNotContainsSpecialSymbol = "Пароль должен содержать специальный символ.";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        if (Objects.isNull(password)) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNull);
            return false;
        }

        if (password.length() < 8) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordLengthLessThen8);
            return false;
        }

        if (!Pattern.compile("[0-9]").matcher(password).find()) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNotContainsNumbers);
            return false;
        }

        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNotContainsUppercaseLetters);
            return false;
        }

        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNotContainsLowercaseLetters);
            return false;
        }

        if (!Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]").matcher(password).find()) {
            createCustomMessage(constraintValidatorContext, messageIfPasswordNotContainsSpecialSymbol);
            return false;
        }

        return true;
    }

    private void createCustomMessage(ConstraintValidatorContext constraintValidatorContext, String message) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }


}
