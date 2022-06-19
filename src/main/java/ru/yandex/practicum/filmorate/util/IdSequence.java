package ru.yandex.practicum.filmorate.util;

public class IdSequence {
    private static long FILM_ID = 0;
    private static long USER_ID = 0;

    public static long getNewFilmId(){
        return ++FILM_ID;
    }
    public static long getNewUserId(){
        return ++USER_ID;
    }
}
