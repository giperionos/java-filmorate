package ru.yandex.practicum.filmorate.exceptions;

public class UnknownFilmException extends RuntimeException {
    public UnknownFilmException(String message){
        super(message);
    }
}
