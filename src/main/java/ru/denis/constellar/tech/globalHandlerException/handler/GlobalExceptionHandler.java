package ru.denis.constellar.tech.globalHandlerException.handler;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.denis.constellar.tech.globalHandlerException.dto.ErrorResponse;
import ru.denis.constellar.tech.auth.exceptions.PasswordConfirmNotEqualsPassword;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handlerValidationException(MethodArgumentNotValidException ex) {

        List<String> errors = ex.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ErrorResponse.builder()
                .timeOfError(LocalDateTime.now())
                .errors(errors)
                .build();
    }

}
