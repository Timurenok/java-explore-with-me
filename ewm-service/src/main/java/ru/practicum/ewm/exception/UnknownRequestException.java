package ru.practicum.ewm.exception;

public class UnknownRequestException extends RuntimeException {
    public UnknownRequestException(String message) {
        super(message);
    }
}
