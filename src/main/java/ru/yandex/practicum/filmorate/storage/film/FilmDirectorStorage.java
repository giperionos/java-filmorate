package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;

public interface FilmDirectorStorage {
    void add(Long filmId, Integer directorId);
    List<FilmDirector> getByFilmId(Long filmId);
    boolean deleteByFilmId(Long id);
}
