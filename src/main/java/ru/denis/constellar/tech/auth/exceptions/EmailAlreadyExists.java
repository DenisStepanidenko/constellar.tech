package ru.denis.constellar.tech.auth.exceptions;


public class EmailAlreadyExists extends RuntimeException {

    public EmailAlreadyExists(String message) {
        super(message);
    }
}
