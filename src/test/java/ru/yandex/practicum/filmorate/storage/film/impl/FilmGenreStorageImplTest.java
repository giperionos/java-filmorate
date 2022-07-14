package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreStorageImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreStorageImplTest {

    private final FilmGenreStorageImpl filmGenreStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreStorageImpl genreStorage;

    private static Film film;
    private static Genre comedyGenre;
    private static Genre cartoonGenre;

    @BeforeAll
    public static void init() {
        film = new Film(
                null,
                "Matrix",
                "Super-Mega Film",
                LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" ),
                null);

        comedyGenre = new Genre(1, "Комедия");
        cartoonGenre = new Genre(3, "Мультфильм");
    }

    @Test
    void testAddFilmGenre() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.add(film);

        //нужно, чтобы были жанры в БД
        genreStorage.add(comedyGenre);
        genreStorage.add(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.add(film.getId(), comedyGenre.getId());
        filmGenreStorage.add(film.getId(), cartoonGenre.getId());

        //получить из БД жанры связанные с фильмом
        List<FilmGenre> filmGenresDb = filmGenreStorage.getByFilmId(film.getId());

        //список ожидаемых связок фильм-жанр
        List<FilmGenre> filmGenresExpected = List.of(
                new FilmGenre(film.getId(), comedyGenre.getId()),
                new FilmGenre(film.getId(), cartoonGenre.getId())
        );


        assertTrue(filmGenresDb.containsAll(filmGenresExpected), "В списке жанров фильма не все жанры.");
    }

    @Test
    void testDeleteFilmGenreByFilmId() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.add(film);

        //нужно, чтобы были жанры в БД
        genreStorage.add(comedyGenre);
        genreStorage.add(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.add(film.getId(), comedyGenre.getId());
        filmGenreStorage.add(film.getId(), cartoonGenre.getId());

        //удалить один жанр для фильма
        filmGenreStorage.deleteByFilmId(film.getId());

        //проверить, что для фильма не осталось связок фильм-жанр
        //получить из БД жанры связанные с фильмом
        List<FilmGenre> filmGenresDb = filmGenreStorage.getByFilmId(film.getId());

        assertTrue(filmGenresDb.isEmpty(), "Список связок фильм-жанр не пустой после удаления.");
    }

    @Test
    void testGetFilmGenresByFilmId() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.add(film);

        //нужно, чтобы были жанры в БД
        genreStorage.add(comedyGenre);
        genreStorage.add(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.add(film.getId(), comedyGenre.getId());
        filmGenreStorage.add(film.getId(), cartoonGenre.getId());

        List<FilmGenre> filmGenresDb = filmGenreStorage.getByFilmId(film.getId());

        //список ожидаемых связок фильм-жанр
        List<FilmGenre> filmGenresExpected = List.of(
                new FilmGenre(film.getId(), comedyGenre.getId()),
                new FilmGenre(film.getId(), cartoonGenre.getId())
        );


        assertTrue(filmGenresDb.containsAll(filmGenresExpected), "В списке жанров фильма не все жанры.");
    }

    @Test
    void testGetAllFilmGenres() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.add(film);

        //нужно, чтобы были жанры в БД
        genreStorage.add(comedyGenre);
        genreStorage.add(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.add(film.getId(), comedyGenre.getId());
        filmGenreStorage.add(film.getId(), cartoonGenre.getId());

        List<FilmGenre> filmGenresDb = filmGenreStorage.getAll();

        //список ожидаемых связок фильм-жанр
        List<FilmGenre> filmGenresExpected = List.of(
                new FilmGenre(film.getId(), comedyGenre.getId()),
                new FilmGenre(film.getId(), cartoonGenre.getId())
        );


        assertTrue(filmGenresDb.containsAll(filmGenresExpected), "В списке жанров фильма не все жанры.");
    }
}