package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDirectorStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final RatingStorage ratingStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;
    private final FilmDirectorStorage filmDirectorStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       RatingStorage ratingStorage,
                       FilmGenreStorage filmGenreStorage,
                       GenreStorage genreStorage,
                       DirectorStorage directorStorage,
                       FilmDirectorStorage filmDirectorStorage,
                       UserStorage userStorage)
    {
        this.filmStorage = filmStorage;
        this.ratingStorage = ratingStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
        this.directorStorage = directorStorage;
        this.filmDirectorStorage = filmDirectorStorage;
        this.userStorage = userStorage;
    }

    public Film add(Film film) {
        Film addedFilm = filmStorage.add(film);

        //Дозаполнить объект рейтинга MPA в составе объекта Film
        fillMPAForFilm(addedFilm);

        //заполнить объекты жанров в составе объекта Film и добавить их БД для данного Film
        addGenreFilmToDB(addedFilm, false);

        //заполнить объекты Режиссеров в составе объекта Film и добавить их БД для данного Film
        addFilmDirectorToDB(addedFilm, false);

        return addedFilm;
    }

    public Film update(Film film) {
        try {
            //обновить фильм в самой таблице Film
            Film updatedFilm = filmStorage.update(film);

            //Дозаполнить объект рейтинга MPA в составе объекта Film
            fillMPAForFilm(updatedFilm);

            //также нужно обновить жанры
            //заполнить объекты жанров в составе объекта Film и добавить их БД для данного Film
            addGenreFilmToDB(updatedFilm, true);

            //заполнить объекты Режиссеров в составе объекта Film и добавить их БД для данного Film
            addFilmDirectorToDB(updatedFilm, true);

            return updatedFilm;

        } catch (EntityNotFoundException exception) {
            throw new UnknownFilmException(String.format("Фильм с %d не найден в хранилище.", film.getId()));
        }

    }

    public List<Film> getAll() {
        List<Film> allFilms = filmStorage.getAll();

        //заполнить жанры и режиссеров для фильмов
        for (Film film: allFilms) {
            fillGenreForFilm(film);
            fillDirectorForFilm(film);
        }

        return allFilms;
    }

    public Film getFilmById(Long id) {
        Film foundedFilm = filmStorage.getById(id);

        if (foundedFilm == null) {
            throw new EntityNotFoundException(String.format("Фильм с %d не найден в хранилище.", id));
        }

        //заполнить жанры для фильма
        fillGenreForFilm(foundedFilm);

        //заполнить режиссеров для фильма
        fillDirectorForFilm(foundedFilm);

        return foundedFilm;
    }

    public List<Film> getPopularFilms(Long count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> popularFilms = filmStorage.getMostPopularList(count, genreId, year);

        //заполнить жанры и режиссеров для фильмов
        for (Film film: popularFilms) {
            fillGenreForFilm(film);
            fillDirectorForFilm(film);
        }

        return popularFilms;
    }

    public List<Film> getFilmsByQuery(String query, String by) {
        final List<Film> films = filmStorage.getFilmsByQuery(query, by);
        films.forEach(this::fillGenreForFilm);
        films.forEach(this::fillDirectorForFilm);
        return films;
    }

    public void deleteFilmById(Long id) {
        filmStorage.deleteById(id);
    }

    public Collection<Film> getRecommendations(Long userId) {
        final Collection<Film> films = filmStorage.getRecommendations(userId);
        films.forEach(this::fillGenreForFilm);
        films.forEach(this::fillDirectorForFilm);
        return films;
    }

    private void fillGenreForFilm(Film film) {

        List<FilmGenre> filmDBGenres = filmGenreStorage.getByFilmId(film.getId());

        //заполнить объекты Жанров в составе объекта Film
        Set<Genre> filmGenresSorted = new TreeSet<>(new Comparator<Genre>() {
            @Override
            public int compare(Genre o1, Genre o2) {
                return -1 * o2.getId().compareTo(o1.getId());
            }
        });


        for (FilmGenre filmGenre : filmDBGenres) {
            filmGenresSorted.add(genreStorage.getById(filmGenre.getGenreId()));
        }

        film.setGenres(filmGenresSorted);
    }

    private void addGenreFilmToDB(Film film, boolean needCleanBeforeAdd) {

        //если нужно добавить в БД
        if (film.getGenres() == null) {
            //но если это апдейт, возможно у предыдущей версии фильма были связки с жанрами,
            //а сейчас их уже нет, значит нужно удалить
            if (needCleanBeforeAdd) {
                filmGenreStorage.deleteByFilmId(film.getId());
            }
            return;
        }

        //нужно ли предварительно почистить таблицу связей с жанрами
        if (needCleanBeforeAdd) {
            //почистить жанры для фильма, прежде чем добавить обновленные
            filmGenreStorage.deleteByFilmId(film.getId());
        }

        for (Genre genre : film.getGenres()) {
            filmGenreStorage.add(film.getId(), genre.getId());
        }

        //заполнить объекты жанров в составе объекта Film
        fillGenreForFilm(film);
    }

    private void fillMPAForFilm(Film film) {

        if (film.getMpa() == null) {
            return;
        }

        //Дозаполнить объект рейтинга MPA в составе объекта Film
        MPARating mpaRating = ratingStorage.getById(film.getMpa().getId());
        film.getMpa().setName(mpaRating.getName());
        film.getMpa().setDescription(mpaRating.getDescription());
    }


    private void addFilmDirectorToDB(Film film, boolean needCleanBeforeAdd) {

        //если режиссеры для фильма не указаны, значит добавлять не нужно
        if (film.getDirectors() == null) {
            //но если это апдейт, возможно у предыдущей версии фильма были связки с режиссерами,
            //а сейчас их уже нет, значит нужно удалить
            if (needCleanBeforeAdd) {
                filmDirectorStorage.deleteByFilmId(film.getId());
            }
            return;
        }

        //нужно ли предварительно почистить прежние связи для данного фильма с режиссерами
        if (needCleanBeforeAdd) {
            filmDirectorStorage.deleteByFilmId(film.getId());
        }

        //для каждого указанного режиссера добавить связку с фильмом
        for (Director director: film.getDirectors()) {
            filmDirectorStorage.add(film.getId(), director.getId());
        }

        //дозаполнить объекты режиссеров в составе объекта Film
        fillDirectorForFilm(film);
    }

    private void fillDirectorForFilm(Film film) {
        List<FilmDirector> filmDBDirectors = filmDirectorStorage.getByFilmId(film.getId());

        //заполнить объекты  Режиссерово в составе объекта Film
        Set<Director> filmDirectorsSorted = new TreeSet<>(new Comparator<Director>() {
            @Override
            public int compare(Director o1, Director o2) {
                return -1 * o2.getId().compareTo(o1.getId());
            }
        });


        for (FilmDirector filmDirector : filmDBDirectors) {
            filmDirectorsSorted.add(directorStorage.getById(filmDirector.getDirectorId()));
        }

        film.setDirectors(filmDirectorsSorted);
    }


    public List<Film> getSortedFilmsForDirector(Integer directorId, String sortBy) {
        //сначала нужно убедиться, что режиссер с таким id вообще есть
        try {
            directorStorage.getById(directorId);
        } catch (EntityNotFoundException e) {
            log.warn("Для поиска фильмов пришел неизвестный режиссер.");
            throw e;
        }

        List<Film> sortedFilms;

        switch (sortBy) {
            case "year":
                //Вывод всех фильмов режиссёра, отсортированных по годам.
                sortedFilms = filmStorage.getFilmsForDirectorSortedByYears(directorId);
                break;

            case "likes":
                //Вывод всех фильмов режиссёра, отсортированных по количеству лайков
                sortedFilms = filmStorage.getFilmsForDirectorSortedByLikes(directorId);
                break;

            default:
                //Вывод всех фильмов режиссёра, отсортированных по годам и по количеству лайков
                sortedFilms = filmStorage.getFilmsForDirectorSortedByYearsAndLikes(directorId);
        }

        //заполнить жанры и режиссеров для фильмов
        for (Film film: sortedFilms) {
            fillGenreForFilm(film);
            fillDirectorForFilm(film);
        }

        if (sortedFilms == null) {
            sortedFilms = new ArrayList<>();
        }

        return sortedFilms;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        //Сначала нужно убедиться, есть ли такие пользователи

        try {
            userStorage.getById(userId);
        } catch (EntityNotFoundException e) {
            log.warn("Пришел неизвестный пользователь с id = " + userId);
            throw e;
        }

        try {
            userStorage.getById(friendId);
        } catch (EntityNotFoundException e) {
            log.warn("Пришел неизвестный пользователь с id = " + friendId);
            throw e;
        }

        return filmStorage.getCommonFilmsByRating(userId, friendId);
    }
}
