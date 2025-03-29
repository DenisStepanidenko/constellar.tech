package ru.denis.constellar.tech.auth.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.denis.constellar.tech.auth.validation.PasswordMatchesValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "Incorrect data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
