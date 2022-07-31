package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.like.impl.LikeStorageDbImpl;
import ru.yandex.practicum.filmorate.storage.user.impl.UserStorageDbImpl;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageDbImplTest {

    private final FilmStorageDbImpl filmDbStorage;
    private final UserStorageDbImpl userDbStorage;
    private final LikeStorageDbImpl likesStorage;

    private Long film1_id = null;
    private String film1_name = "Matrix";
    private String film1_description = "Super-Mega Film";
    private LocalDate film1_releaseDate = LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);
    private Integer film1_duration = 136;
    private MpaRating film1_mpa = new MpaRating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" );
    private Set<Genre> film1_genres = null;
    private Set<Director> film1_directors = null;

    private Long film2_id = null;
    private String film2_name = "Who am I?";
    private String film2_description = "nice movie";
    private LocalDate film2_releaseDate = LocalDate.parse("17.01.1998", FilmorateConfig.NORMAL_DATE_FORMATTER);
    private Integer film2_duration = 136;
    private MpaRating film2_mpa = new MpaRating(3, "PG-13", "Детям до 13 лет просмотр не желателен" );
    private Set<Genre> film2_genres = null;
    private Set<Director> film2_directors = null;

    private Long film3_id = null;
    private String film3_name = "It Takes Two";
    private String film3_description = "Kind movie";
    private LocalDate film3_releaseDate = LocalDate.parse("17.11.1995", FilmorateConfig.NORMAL_DATE_FORMATTER);
    private Integer film3_duration = 136;
    private MpaRating film3_mpa = new MpaRating(3, "PG-13", "Детям до 13 лет просмотр не желателен" );
    private Set<Genre> film3_genres = null;
    private Set<Director> film3_directors = null;

    private Long user1_id = null;
    private String user1_email = "test@test.ru";
    private String user1_login = "login";
    private String user1_name = "Name";
    private LocalDate user1_birthday = LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);

    private Long user2_id = null;
    private String user2_email = "test2@test.ru";
    private String user2_login = "login2";
    private String user2_name = "Name2";
    private LocalDate user2_birthday = LocalDate.parse("15.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);

    private Long user3_id = null;
    private String user3_email = "test3@test.ru";
    private String user3_login = "login3";
    private String user3_name = "Name3";
    private LocalDate user3_birthday = LocalDate.parse("16.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);

    private Film film1 = new Film(film1_id, film1_name, film1_description, film1_releaseDate, film1_duration, film1_mpa,
                film1_genres, film1_directors);

    private Film film2 = new Film(film2_id, film2_name, film2_description, film2_releaseDate, film2_duration, film2_mpa,
                film2_genres, film2_directors);

    private Film film3 = new Film(film3_id, film3_name, film3_description, film3_releaseDate, film3_duration, film3_mpa,
                film3_genres, film3_directors);

    User user1 = new User(user1_id, user1_email, user1_login, user1_name, user1_birthday);
    User user2 = new User(user2_id, user2_email, user2_login, user2_name, user2_birthday);
    User user3 = new User(user3_id, user3_email, user3_login, user3_name, user3_birthday);

    @Test
    void testAddFilm() {
        filmDbStorage.addNewFilm(film2);

        List<Film> dbFilms = filmDbStorage.getAllFilms();

        assertTrue(dbFilms.contains(film2), "Среди фильмов в БД нет добавленного.");
    }

    @Test
    void testUpdateFilm() {
        filmDbStorage.addNewFilm(film2);

        film2.setDescription("very cool Film");
        film2.setDuration(102);

        filmDbStorage.updateFilm(film2);

        Film updatedFilm = filmDbStorage.getFilmById(film2.getId());

        assertEquals("very cool Film", updatedFilm.getDescription(), "Описание фильма в БД не обновилось.");
        assertEquals(102, updatedFilm.getDuration(), "Длительность фильма в БД не обновилась.");
    }

    @Test
    void testGetAllFilms() {
        filmDbStorage.addNewFilm(film1);
        filmDbStorage.addNewFilm(film2);
        filmDbStorage.addNewFilm(film3);

        List<Film> dbFilms = filmDbStorage.getAllFilms();

        assertTrue(dbFilms.containsAll(List.of(film1, film2, film3)), "Список из из БД не содержит нужные фильмы.");
    }

    @Test
    void testGetFilmById() {
        filmDbStorage.addNewFilm(film3);

        Film expectedFilm = filmDbStorage.getFilmById(film3.getId());
        assertEquals(expectedFilm, film3, "Получен не тот фильм из БД.");
    }

    @Test
    void getMostPopularFilmsList() {
        //перед вставкой новых данных
        userDbStorage.deleteAll();
        filmDbStorage.deleteAll();
        likesStorage.deleteAllLikes();

        userDbStorage.addNewUser(user1);
        userDbStorage.addNewUser(user2);
        userDbStorage.addNewUser(user3);

        //теперь нужны фильмы
        filmDbStorage.addNewFilm(film1);
        filmDbStorage.addNewFilm(film2);
        filmDbStorage.addNewFilm(film3);

        //теперь нужны лайки к фильмам
        //пусть у фильма 1 будет два лайка
        likesStorage.addNewLikeForFilmIdByUserId(film1.getId(), user1.getId());
        likesStorage.addNewLikeForFilmIdByUserId(film1.getId(), user2.getId());

        //пусть у фильма 3 будет 3 лайка, а у фильма 2 не будет лайков
        likesStorage.addNewLikeForFilmIdByUserId(film3.getId(), user1.getId());
        likesStorage.addNewLikeForFilmIdByUserId(film3.getId(), user2.getId());
        likesStorage.addNewLikeForFilmIdByUserId(film3.getId(), user3.getId());

        //получить список популярных
        List<Film> popularFilms = filmDbStorage.getMostPopularFilmsList(10L, Optional.empty(), Optional.empty());

        //Должны фильмы в порядке: 3 1 2
        assertEquals(film3.getId(), popularFilms.get(0).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");
        assertEquals(film1.getId(), popularFilms.get(1).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");
        assertEquals(film2.getId(), popularFilms.get(2).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");

        //проверить ограничения на выдачу кол-во элементов списка
        List<Film> popular2Films = filmDbStorage.getMostPopularFilmsList(2L, Optional.empty(), Optional.empty());
        assertEquals(2, popular2Films.size(), "Количество элементов списка не совпадает с ожидаемым.");
    }
}
