package ru.practicum.ewm.exception;

public class UnknownCategoryException extends RuntimeException {
    public UnknownCategoryException(String message) {
        super(message);
    }
}
