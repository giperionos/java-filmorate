package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {

    void addNewFilmGenreLink(Long filmId, Integer genreId);

    boolean deleteFilmGenreLinksByFilmId(Long filmId);

    List<FilmGenre> getFilmGenreLinksByFilmId(Long filmId);

    List<FilmGenre> getAllFilmGenreLinks();
}
