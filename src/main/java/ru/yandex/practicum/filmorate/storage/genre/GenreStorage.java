package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getById(Integer id);
    List<Genre> getAll();
    void add(Genre genre);
}
