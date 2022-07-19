package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.film.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private RatingStorage ratingStorage;
    private FilmGenreStorage filmGenreStorage;
    private GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, RatingStorage ratingStorage, FilmGenreStorage filmGenreStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.ratingStorage = ratingStorage;
        this.filmGenreStorage = filmGenreStorage;
        this.genreStorage = genreStorage;
    }

    public Film add(Film film) {
        Film addedFilm = filmStorage.add(film);

        //Дозаполнить объект рейтинга MPA в составе объекта Film
        fillMPAForFilm(addedFilm);

        //заполнить объекты жанров в составе объекта Film и добавить их БД для данного Film
        addGenreFilmToDB(addedFilm, false);

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

            return updatedFilm;

        } catch (EntityNotFoundException exception) {
            throw new UnknownFilmException(String.format("Фильм с %d не найден в хранилище.", film.getId()));
        }

    }

    public List<Film> getAll() {
        List<Film> allFilms = filmStorage.getAll();

        //заполнить жанры для фильмов
        for (Film film: allFilms) {
            fillGenreForFilm(film);
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

        return foundedFilm;
    }

    public List<Film> getPopularFilms(Long count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> popularFilms = filmStorage.getMostPopularList(count, genreId, year);

        //заполнить жанры для фильмов
        for (Film film: popularFilms) {
            fillGenreForFilm(film);
        }

        return popularFilms;
    }

    public void deleteFilmById(Long id) {
        filmStorage.deleteById(id);
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

    private void addGenreFilmToDB(Film film, boolean needCleanBeforeAdd){

        //если нужно добавить в БД
        if (film.getGenres() == null) {
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
}
