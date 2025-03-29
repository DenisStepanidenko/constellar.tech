package ru.denis.constellar.tech.auth.exceptions;


public class PasswordConfirmNotEqualsPassword extends RuntimeException {

    public PasswordConfirmNotEqualsPassword(String message) {
        super(message);
    }
}
