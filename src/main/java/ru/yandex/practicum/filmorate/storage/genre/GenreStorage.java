package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenreById(Integer genreId);

    List<Genre> getAllGenres();

    void addNewGenre(Genre genre);
}
