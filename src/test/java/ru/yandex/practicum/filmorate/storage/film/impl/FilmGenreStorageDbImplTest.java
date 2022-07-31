package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.genre.impl.GenreStorageDbImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmGenreStorageDbImplTest {

    private final FilmGenreStorageDbImpl filmGenreStorage;
    private final FilmStorageDbImpl filmDbStorage;
    private final GenreStorageDbImpl genreStorage;

    private Long film1_id = null;
    private String film1_name = "Matrix";
    private String film1_description = "Super-Mega Film";
    private LocalDate film1_releaseDate = LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);
    private Integer film1_duration = 136;
    private MpaRating film1_mpa = new MpaRating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" );
    private Set<Genre> film1_genres = null;
    private Set<Director> film1_directors = null;

    private Film film = new Film(film1_id, film1_name, film1_description, film1_releaseDate, film1_duration, film1_mpa,
                film1_genres, film1_directors);

    private Genre comedyGenre = new Genre(1, "Комедия");
    private Genre cartoonGenre = new Genre(3, "Мультфильм");

    @Test
    void testAddFilmGenre() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.addNewFilm(film);

        //нужно, чтобы были жанры в БД
        genreStorage.addNewGenre(comedyGenre);
        genreStorage.addNewGenre(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.addNewFilmGenreLink(film.getId(), comedyGenre.getId());
        filmGenreStorage.addNewFilmGenreLink(film.getId(), cartoonGenre.getId());

        //получить из БД жанры связанные с фильмом
        List<FilmGenre> filmGenresDb = filmGenreStorage.getFilmGenreLinksByFilmId(film.getId());

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
        filmDbStorage.addNewFilm(film);

        //нужно, чтобы были жанры в БД
        genreStorage.addNewGenre(comedyGenre);
        genreStorage.addNewGenre(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.addNewFilmGenreLink(film.getId(), comedyGenre.getId());
        filmGenreStorage.addNewFilmGenreLink(film.getId(), cartoonGenre.getId());

        //удалить один жанр для фильма
        filmGenreStorage.deleteFilmGenreLinksByFilmId(film.getId());

        //проверить, что для фильма не осталось связок фильм-жанр
        //получить из БД жанры связанные с фильмом
        List<FilmGenre> filmGenresDb = filmGenreStorage.getFilmGenreLinksByFilmId(film.getId());

        assertTrue(filmGenresDb.isEmpty(), "Список связок фильм-жанр не пустой после удаления.");
    }

    @Test
    void testGetFilmGenresByFilmId() {
        //сначала нужно, чтобы был фильм в БД
        filmDbStorage.addNewFilm(film);

        //нужно, чтобы были жанры в БД
        genreStorage.addNewGenre(comedyGenre);
        genreStorage.addNewGenre(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.addNewFilmGenreLink(film.getId(), comedyGenre.getId());
        filmGenreStorage.addNewFilmGenreLink(film.getId(), cartoonGenre.getId());

        List<FilmGenre> filmGenresDb = filmGenreStorage.getFilmGenreLinksByFilmId(film.getId());

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
        filmDbStorage.addNewFilm(film);

        //нужно, чтобы были жанры в БД
        genreStorage.addNewGenre(comedyGenre);
        genreStorage.addNewGenre(cartoonGenre);

        //добавить для фильма жанры
        filmGenreStorage.addNewFilmGenreLink(film.getId(), comedyGenre.getId());
        filmGenreStorage.addNewFilmGenreLink(film.getId(), cartoonGenre.getId());

        List<FilmGenre> filmGenresDb = filmGenreStorage.getAllFilmGenreLinks();

        //список ожидаемых связок фильм-жанр
        List<FilmGenre> filmGenresExpected = List.of(
                new FilmGenre(film.getId(), comedyGenre.getId()),
                new FilmGenre(film.getId(), cartoonGenre.getId())
        );

        assertTrue(filmGenresDb.containsAll(filmGenresExpected), "В списке жанров фильма не все жанры.");
    }
}