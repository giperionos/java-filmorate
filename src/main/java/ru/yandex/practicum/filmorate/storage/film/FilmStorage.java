package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {
    //методы свойственные именно для Film

    List<Film> getMostPopularList(Long count, Optional<Integer> genreId, Optional<Integer> year);

}
