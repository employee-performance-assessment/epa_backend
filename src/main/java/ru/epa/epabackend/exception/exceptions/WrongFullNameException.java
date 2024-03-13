package ru.epa.epabackend.exception.exceptions;

public class WrongFullNameException extends RuntimeException {
    public WrongFullNameException(String message) {
        super(message);
    }
}