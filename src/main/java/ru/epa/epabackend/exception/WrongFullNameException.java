package ru.epa.epabackend.exception;

public class WrongFullNameException extends RuntimeException {
    public WrongFullNameException(String message) {
        super(message);
    }
}
