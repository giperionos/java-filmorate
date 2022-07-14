package ru.yandex.practicum.filmorate.storage.likes.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikesStorageImplTest {

    private final LikesStorageImpl likesStorage;

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private User user;
    private Film film;

    @BeforeEach
    void init(){
        user = new User(null, "test@test.ru", "login", "name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
        userStorage.add(user);

        film = new Film(null,
                "Matrix",
                "Super-Mega Film",
                LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(1,null, null),
                null
        );

        filmStorage.add(film);
    }

    @Test
    void testAddLike() {

        int beforeAdd = likesStorage.getAll().size();
        likesStorage.add(film.getId(), user.getId());

        int afterAdd = likesStorage.getAll().size();
        assertEquals(beforeAdd +1, afterAdd, "Не верное количество записей в таблице.");

    }

    @Test
    void testDeleteLike() {
        likesStorage.add(film.getId(), user.getId());
        boolean isDeleted = likesStorage.delete(film.getId(), user.getId());

        assertTrue(isDeleted, "Удаление не случилось.");
    }
}