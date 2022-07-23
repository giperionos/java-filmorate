package ru.yandex.practicum.filmorate.storage.likes.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.impl.FilmStorageDbImpl;
import ru.yandex.practicum.filmorate.storage.like.impl.LikeStorageDbImpl;
import ru.yandex.practicum.filmorate.storage.user.impl.UserStorageDbImpl;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LikeStorageDbImplTest {

    private final LikeStorageDbImpl likesStorage;

    private final FilmStorageDbImpl filmStorage;
    private final UserStorageDbImpl userStorage;

    private User user;
    private Film film;

    private static Long film1_id = null;
    private static String film1_name = "Matrix";
    private static String film1_description = "Super-Mega Film";
    private static LocalDate film1_releaseDate = LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter);
    private static Integer film1_duration = 136;
    private static MPARating film1_mpa = new MPARating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" );
    private static Set<Genre> film1_genres = null;
    private static Set<Director> film1_directors = null;

    private Long user1_id = null;
    private String user1_email = "test@test.ru";
    private String user1_login = "login";
    private String user1_name = "Name";
    private LocalDate user1_birthday = LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter);

    @BeforeEach
    void init(){
        user = new User(user1_id, user1_email, user1_login, user1_name, user1_birthday);
        userStorage.addNewUser(user);

        film = new Film(
                film1_id,
                film1_name,
                film1_description,
                film1_releaseDate,
                film1_duration,
                film1_mpa,
                film1_genres,
                film1_directors);

        filmStorage.addNewFilm(film);
    }

    @Test
    void testAddLike() {

        int beforeAdd = likesStorage.getAllLikes().size();
        likesStorage.addNewLikeForFilmIdByUserId(film.getId(), user.getId());

        int afterAdd = likesStorage.getAllLikes().size();
        assertEquals(beforeAdd +1, afterAdd, "Не верное количество записей в таблице.");

    }

    @Test
    void testDeleteLike() {
        likesStorage.addNewLikeForFilmIdByUserId(film.getId(), user.getId());
        boolean isDeleted = likesStorage.deleteLikeForFilmIdByUserId(film.getId(), user.getId());

        assertTrue(isDeleted, "Удаление не случилось.");
    }
}