package ru.denis.constellar.tech.auth.exceptions;

public class PasswordOrEmailIncorrect extends RuntimeException {

    public PasswordOrEmailIncorrect(String message) {
        super(message);
    }

}
