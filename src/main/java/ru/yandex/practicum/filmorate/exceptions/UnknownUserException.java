package ru.yandex.practicum.filmorate.exceptions;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException(String message){
        super(message);
    }
}
