package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageDbImplTest {

    private  final UserStorageDbImpl userDbStorage;
    private static User user;
    private static Long user_id_null = null;

    @BeforeAll
    public static void beforeAll(){
        user = new User(user_id_null, "test@test.ru", "login", "name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
    }

    @Test
    void testAddUser() {
        userDbStorage.addNewUser(user);
        User addedUser = userDbStorage.getUserById(user.getId());

        assertEquals(user, addedUser, "Добавленный пользователь не совпадает с исходным.");
    }

    @Test
    void testUpdate() {
        userDbStorage.addNewUser(user);

        user.setEmail("test@test.ru updateFilm");
        user.setLogin("login updateFilm");
        user.setName("name updateFilm");

        userDbStorage.updateUser(user);
        User updatedUser = userDbStorage.getUserById(user.getId());

        assertEquals(user.getId(), updatedUser.getId(), "Id не должен был поменяться.");
        assertEquals("test@test.ru updateFilm", updatedUser.getEmail(), "Не изменился email");
        assertEquals("login updateFilm", updatedUser.getLogin(), "Не изменился login");
        assertEquals("name updateFilm", updatedUser.getName(), "Не изменился name");
    }

    @Test
    void testGetAll() {
        userDbStorage.addNewUser(user);

        List<User> users = userDbStorage.getAllUsers();
        assertFalse(users.isEmpty(), "Размер полученного списка пустой.");
    }

    @Test
    void testGetById() {
         userDbStorage.addNewUser(user);

         User foundedUser = userDbStorage.getUserById(user.getId());
         assertEquals(user, foundedUser, "Полученный пользователь не совпадает с ожидаемым.");
    }
}