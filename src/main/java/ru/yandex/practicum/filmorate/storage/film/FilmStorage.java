package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {
    //методы свойственные именно для Film

    List<Film> getMostPopularList(Long count, Optional<Integer> genreId, Optional<Integer> year);

    List<Film> getFilmsForDirectorSortedByYears(Integer directorId);

    List<Film> getFilmsForDirectorSortedByLikes(Integer directorId);

    List<Film> getFilmsForDirectorSortedByYearsAndLikes(Integer directorId);

    Collection<Film> getRecommendations(Long userId);

    List<Film> getFilmsByQuery(String query, String by);

    List<Film> getCommonFilmsByRating(Long userId, Long friendId);
}
