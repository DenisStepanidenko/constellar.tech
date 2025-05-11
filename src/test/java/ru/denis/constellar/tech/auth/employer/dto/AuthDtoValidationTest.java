package ru.denis.constellar.tech.auth.employer.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import ru.denis.constellar.tech.auth.employer.dto.CompanyRegistrationRequest;
import ru.denis.constellar.tech.auth.employer.dto.RequestCompanyLogin;

import static org.junit.jupiter.api.Assertions.*;

class AuthDtoValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validCompanyRegistrationRequest_ShouldPassValidation() {
        CompanyRegistrationRequest request = createValidRegistrationRequest();
        assertEquals(0, validator.validate(request).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "a", "ThisNameIsWayTooLongForTheValidationConstraintsSetInTheAnnotation"})
    void invalidCompanyName_ShouldFailValidation(String companyName) {
        CompanyRegistrationRequest request = createValidRegistrationRequest();
        request.setCompanyName(companyName);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void validLoginRequest_ShouldPassValidation() {
        RequestCompanyLogin request = new RequestCompanyLogin();
        request.setEmail("valid@example.com");
        request.setPassword("password123");
        assertEquals(0, validator.validate(request).size());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "invalid", "test@"})
    void invalidEmail_ShouldFailValidation(String email) {
        RequestCompanyLogin request = new RequestCompanyLogin();
        request.setEmail(email);
        request.setPassword("password123");
        assertFalse(validator.validate(request).isEmpty());
    }

    private CompanyRegistrationRequest createValidRegistrationRequest() {
        CompanyRegistrationRequest request = new CompanyRegistrationRequest();
        request.setCompanyName("Valid Company");
        request.setInn("1234567890");
        request.setEmail("valid@example.com");
        request.setPassword("ValidPass123!");
        request.setConfirmPassword("ValidPass123!");
        return request;
    }
}