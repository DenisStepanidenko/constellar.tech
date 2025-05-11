package ru.denis.constellar.tech.globalHandlerException.handler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.globalHandlerException.dto.ErrorResponse;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleValidationException_ReturnsCorrectErrorResponse() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError1 = new FieldError("object", "field1", "Error message 1");
        FieldError fieldError2 = new FieldError("object", "field2", "Error message 2");

        when(ex.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

        // Act
        ErrorResponse response = globalExceptionHandler.handleValidationException(ex);

        // Assert
        assertNotNull(response.getTimeOfError());
        assertEquals(2, response.getErrors().size());
        assertTrue(response.getErrors().contains("Error message 1"));
        assertTrue(response.getErrors().contains("Error message 2"));
        assertTrue(response.getTimeOfError().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void handleEmailAlreadyExistsException_ReturnsCorrectErrorResponse() {
        // Arrange
        String errorMessage = "Email already exists";
        EmailAlreadyExists ex = new EmailAlreadyExists(errorMessage);

        // Act
        ErrorResponse response = globalExceptionHandler.handleEmailAlreadyExistsException(ex);

        // Assert
        assertNotNull(response.getTimeOfError());
        assertEquals(1, response.getErrors().size());
        assertEquals(errorMessage, response.getErrors().get(0));
        assertTrue(response.getTimeOfError().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void handlePasswordOrEmailIncorrectException_ReturnsCorrectErrorResponse() {
        // Arrange
        String errorMessage = "Password or email is incorrect";
        PasswordOrEmailIncorrect ex = new PasswordOrEmailIncorrect(errorMessage);

        // Act
        ErrorResponse response = globalExceptionHandler.handlePasswordOrEmailIncorrectException(ex);

        // Assert
        assertNotNull(response.getTimeOfError());
        assertEquals(1, response.getErrors().size());
        assertEquals(errorMessage, response.getErrors().get(0));
        assertTrue(response.getTimeOfError().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void handleUnauthorized_ReturnsRedirectResponse() {
        // Arrange
        UnauthorizedAccessException ex = new UnauthorizedAccessException();

        // Act
        ResponseEntity<Void> response = globalExceptionHandler.handleUnauthorized();

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(URI.create("http://localhost:8080/home"), response.getHeaders().getLocation());
        assertNull(response.getBody());
    }

    @Test
    void handleValidationException_WithEmptyErrors_ReturnsEmptyList() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getFieldErrors()).thenReturn(Collections.emptyList());

        // Act
        ErrorResponse response = globalExceptionHandler.handleValidationException(ex);

        // Assert
        assertNotNull(response.getTimeOfError());
        assertTrue(response.getErrors().isEmpty());
    }


}