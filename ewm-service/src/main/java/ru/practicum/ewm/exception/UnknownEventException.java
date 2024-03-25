package ru.practicum.ewm.exception;

public class UnknownEventException extends RuntimeException {
    public UnknownEventException(String message) {
        super(message);
    }
}
