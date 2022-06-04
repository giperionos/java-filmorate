package ru.yandex.practicum.filmorate.util;

public class IdSequence {
    private static long ID = 0;

    public static long getNewId(){
        return ++ID;
    }
}
