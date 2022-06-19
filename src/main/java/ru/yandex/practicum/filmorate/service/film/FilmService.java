package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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

    public void putLike(Long id, Long userId) {

        //получить фильм по id из хранилища
        Film film = filmStorage.getById(id);

        //если фильма с таким id нет
        if (film == null) {
            throw new EntityNotFoundException(String.format("Фильм с %d не найден в хранилище.", id));
        }

        //если пользователя с таким userId нет
        if (userStorage.getById(userId) ==  null ) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", id));
        }

        //добавить like этому фильму от пользователя
        film.getLikes().add(userId);
    }

    public void deleteLike(Long id, Long userId) {
        //получить фильм по id из хранилища
        Film film = filmStorage.getById(id);

        //если фильма с таким id нет
        if (film == null) {
            throw new EntityNotFoundException(String.format("Фильм с %d не найден в хранилище.", id));
        }

        //если пользователя с таким userId нет
        if (userStorage.getById(userId) ==  null ) {
            throw new EntityNotFoundException(String.format("Пользователь с %d не найден в хранилище.", id));
        }

        //удалить like этому фильму от пользователя
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getMostPopularList(count);
    }
}
