package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addNewFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Long filmId);

    void deleteFilmById(Long filmId);

    List<Film> getMostPopularFilmsList(Long count, Optional<Integer> genreId, Optional<Integer> year);

    List<Film> getFilmsForDirectorSortedByYears(Integer directorId);

    List<Film> getFilmsForDirectorSortedByLikes(Integer directorId);

    List<Film> getFilmsForDirectorSortedByYearsAndLikes(Integer directorId);

    Collection<Film> getRecommendations(Long userId);

    List<Film> getFilmsByQuery(String query, String queryParam);

    List<Film> getCommonFilmsByRating(Long userId, Long friendId);
}
