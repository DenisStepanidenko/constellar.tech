package ru.denis.constellar.tech.auth.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.denis.constellar.tech.auth.validation.PasswordConstraintValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Incorrect data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
