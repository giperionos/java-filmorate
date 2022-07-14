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
class UserDbStorageTest {

    private  final UserDbStorage userDbStorage;
    private static User user;

    @BeforeAll
    public static void beforeAll(){
        user = new User(null, "test@test.ru", "login", "name",  LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter));
    }

    @Test
    void testAddUser() {
        userDbStorage.add(user);
        User addedUser = userDbStorage.getById(user.getId());

        assertEquals(user, addedUser, "Добавленный пользователь не совпадает с исходным.");
    }

    @Test
    void testUpdate() {
        userDbStorage.add(user);

        user.setEmail("test@test.ru update");
        user.setLogin("login update");
        user.setName("name update");

        userDbStorage.update(user);
        User updatedUser = userDbStorage.getById(user.getId());

        assertEquals(user.getId(), updatedUser.getId(), "Id не должен был поменяться.");
        assertEquals("test@test.ru update", updatedUser.getEmail(), "Не изменился email");
        assertEquals("login update", updatedUser.getLogin(), "Не изменился login");
        assertEquals("name update", updatedUser.getName(), "Не изменился name");
    }

    @Test
    void testGetAll() {
        userDbStorage.add(user);

        List<User> users = userDbStorage.getAll();
        assertFalse(users.isEmpty(), "Размер полученного списка пустой.");
    }

    @Test
    void testGetById() {
         userDbStorage.add(user);

         User foundedUser = userDbStorage.getById(user.getId());
         assertEquals(user, foundedUser, "Полученный пользователь не совпадает с ожидаемым.");
    }
}