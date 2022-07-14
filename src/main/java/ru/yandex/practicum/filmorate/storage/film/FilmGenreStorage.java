package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {

    void add(Long filmId, Integer genreId);
    boolean deleteByFilmId(Long id);
    List<FilmGenre> getByFilmId(Long id);
    List<FilmGenre> getAll();
}
