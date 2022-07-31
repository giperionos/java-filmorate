package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmDirector;

import java.util.List;

public interface FilmDirectorStorage {

    void addNewFilmDirectorLink(Long filmId, Integer directorId);

    List<FilmDirector> getFilmDirectorLinksByFilmId(Long filmId);

    boolean deleteFilmDirectorLinksByFilmId(Long filmId);
}
