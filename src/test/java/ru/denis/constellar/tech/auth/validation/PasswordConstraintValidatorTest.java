package ru.denis.constellar.tech.auth.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordConstraintValidatorTest {

    private PasswordConstraintValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder builder;

    @BeforeEach
    void beforeEach() {

        validator = new PasswordConstraintValidator();

        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
        when(builder.addConstraintViolation()).thenReturn(context);
    }

    @Test
    void testWhenPasswordIsNull() {

        //GIVEN
        String password = null;

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль не должен равняться null.");

    }

    @Test
    void testWhenPasswordLengthLessThen8() {

        //GIVEN
        String password = "test";

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль должен содержать минимум 8 символов.");

    }

    @Test
    void testWhenPasswordNotContainsNumbers() {

        //GIVEN
        String password = "testpassword";

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль должен содержать цифры.");
    }

    @Test
    void testWhenPasswordNotContainsUppercaseLetters() {

        //WHEN
        String password = "testpassword1";

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль должен содержать заглавные буквы.");

    }

    @Test
    void testWhenPasswordNotContainsLowercaseLetters() {

        //GIVEN
        String password = "TESTPASSWORD1";

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль должен содержать строчные буквы.");

    }

    @Test
    void testWhenPasswordNotContainsSpecialSymbol() {

        //GIVEN
        String password = "TESTPASSWORD1t";

        //WHEN
        boolean isValid = validPassword(password);

        //THEN
        assertInvalidPassword(isValid, "Пароль должен содержать специальный символ.");

    }


    private void assertInvalidPassword(boolean isValid, String message) {

        assertFalse(isValid);
        verify(context).buildConstraintViolationWithTemplate(message);

    }

    private boolean validPassword(String password) {

        return validator.isValid(password, context);

    }


}