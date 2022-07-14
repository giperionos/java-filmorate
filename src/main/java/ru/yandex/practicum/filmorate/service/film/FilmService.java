package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        try {
            return filmStorage.update(film);
        } catch (EntityNotFoundException exception) {
            throw new UnknownFilmException(String.format("Фильм с %d не найден в хранилище.", film.getId()));
        }

    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        Film foundedFilm = filmStorage.getById(id);

        if (foundedFilm == null) {
            throw new EntityNotFoundException(String.format("Фильм с %d не найден в хранилище.", id));
        }

        return foundedFilm;
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getMostPopularList(count);
    }
}
