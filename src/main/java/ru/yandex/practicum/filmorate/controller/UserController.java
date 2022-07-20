package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.like.LikeService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final FilmService filmService;

    @Autowired
    public UserController(UserService userService, FilmService filmService) {
        this.userService = userService;
        this.filmService = filmService;
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.debug("Обработка POST запроса по пути /users на добавление пользователя.");
        log.debug("Пришел объект: " + user);
        log.debug("Пользователь: " + user + "сохранен и передан клиенту.");

        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обработка PUT запроса по пути /users на обновление фильма.");
        log.debug("Пришел объект: " + user);

        //и вернуть его
        log.debug("Фильм: " + user + " обновлен и передан клиенту.");
        return userService.update(user);
    }

    @GetMapping
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllUserFriends(@PathVariable Long id) {
        return userService.getAllUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonUserFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonUserFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void removeUserById(@PathVariable Long id) {
        log.debug(String.format("Обработка DELETE запроса по пути /users на удаление пользователя id=%d", id));
        userService.removeUserById(id);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecomendations(@PathVariable Long id) {
        return filmService.getRecomendations(id);
    }
}
