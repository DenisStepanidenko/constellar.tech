package ru.denis.constellar.tech.globalHandlerException.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.denis.constellar.tech.auth.exceptions.EmailAlreadyExists;
import ru.denis.constellar.tech.auth.exceptions.PasswordOrEmailIncorrect;
import ru.denis.constellar.tech.auth.exceptions.UnauthorizedAccessException;
import ru.denis.constellar.tech.globalHandlerException.dto.ErrorResponse;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ErrorResponse.builder()
                .timeOfError(LocalDateTime.now())
                .errors(errors)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailAlreadyExists.class)
    public ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExists ex) {

        return ErrorResponse.builder()
                .timeOfError(LocalDateTime.now())
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordOrEmailIncorrect.class)
    public ErrorResponse handlePasswordOrEmailIncorrectException(PasswordOrEmailIncorrect ex) {
        return ErrorResponse.builder()
                .timeOfError(LocalDateTime.now())
                .errors(Collections.singletonList(ex.getMessage()))
                .build();
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<Void> handleUnauthorized() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/home"))
                .build();
    }


}
