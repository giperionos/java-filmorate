package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.like.impl.LikeStorageImpl;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeStorageImpl likesStorage;

    private static Film film1;
    private static Film film2;
    private static Film film3;

    @BeforeAll
    public static void init() {
        film1 = new Film(
                null,
                "Matrix",
                "Super-Mega Film",
                LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" ),
                null);

        film2 = new Film(
                null,
                "Who am I?",
                "nice movie",
                LocalDate.parse("17.01.1998", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(3, "PG-13", "Детям до 13 лет просмотр не желателен" ),
                null);

        film3 = new Film(
                null,
                "It Takes Two",
                "Kind movie",
                LocalDate.parse("17.11.1995", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(3, "PG-13", "Детям до 13 лет просмотр не желателен" ),
                null);
    }

    @Test
    void testAddFilm() {

        filmDbStorage.add(film2);

        List<Film> dbFilms = filmDbStorage.getAll();

        assertTrue(dbFilms.contains(film2), "Среди фильмов в БД нет добавленного.");
    }

    @Test
    void testUpdateFilm() {
        filmDbStorage.add(film2);

        film2.setDescription("very cool Film");
        film2.setDuration(102);

        filmDbStorage.update(film2);

        Film updatedFilm = filmDbStorage.getById(film2.getId());

        assertEquals("very cool Film", updatedFilm.getDescription(), "Описание фильма в БД не обновилось.");
        assertEquals(102, updatedFilm.getDuration(), "Длительность фильма в БД не обновилась.");
    }

    @Test
    void testGetAllFilms() {
        filmDbStorage.add(film1);
        filmDbStorage.add(film2);
        filmDbStorage.add(film3);

        List<Film> dbFilms = filmDbStorage.getAll();

        assertTrue(dbFilms.containsAll(List.of(film1, film2, film3)), "Список из из БД не содержит нужные фильмы.");
    }

    @Test
    void testGetFilmById() {
        filmDbStorage.add(film3);

        Film expectedFilm = filmDbStorage.getById(film3.getId());
        assertEquals(expectedFilm, film3, "Получен не тот фильм из БД.");
    }

    @Test
    void getMostPopularFilmsList() {
        //перед вставкой новых данных
        userDbStorage.deleteAll();
        filmDbStorage.deleteAll();
        likesStorage.deleteAll();

        //сначала нужен пользователь
        User user1 = new User(null, "test@test.ru", "login", "name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        User user2 = new User(null, "test2@test.ru", "login2", "name2",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        User user3 = new User(null, "test3@test.ru", "login3", "name3",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userDbStorage.add(user1);
        userDbStorage.add(user2);
        userDbStorage.add(user3);

        //теперь нужны фильмы
        filmDbStorage.add(film1);
        filmDbStorage.add(film2);
        filmDbStorage.add(film3);

        //теперь нужны лайки к фильмам
        //пусть у фильма 1 будет два лайка
        likesStorage.add(film1.getId(), user1.getId());
        likesStorage.add(film1.getId(), user2.getId());

        //пусть у фильма 3 будет 3 лайка
        likesStorage.add(film3.getId(), user1.getId());
        likesStorage.add(film3.getId(), user2.getId());
        likesStorage.add(film3.getId(), user3.getId());

        //пусть у фильма 2 не будет лайков

        //получить список популярных
        List<Film> popularFilms = filmDbStorage.getMostPopularList(10L);

        //Должны фильмы в порядке: 3 1 2
        assertEquals(film3.getId(), popularFilms.get(0).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");
        assertEquals(film1.getId(), popularFilms.get(1).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");
        assertEquals(film2.getId(), popularFilms.get(2).getId(), "Ожидаемый фильм не находится в нужной позиции списка.");


        //проверить ограничения на выдачу кол-во элементов списка
        List<Film> popular2Films = filmDbStorage.getMostPopularList(2L);
        assertEquals(2, popular2Films.size(), "Количество элементов списка не совпадает с ожидаемым.");
    }
}