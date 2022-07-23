package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
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
    private final EventService eventService;

    private static final EventType EVENT_FRIEND = EventType.of(3, "FRIEND");

    @Autowired
    public UserController(UserService userService, FilmService filmService, EventService eventService) {
        this.userService = userService;
        this.filmService = filmService;
        this.eventService = eventService;
    }

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        log.debug("Обработка POST запроса по пути /users на добавление пользователя.");
        log.debug("Пришел объект: " + user);
        log.debug("Пользователь: " + user + "сохранен и передан клиенту.");

        return userService.addNewUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Обработка PUT запроса по пути /users на обновление фильма.");
        log.debug("Пришел объект: " + user);

        //и вернуть его
        log.debug("Фильм: " + user + " обновлен и передан клиенту.");
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addToFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addToFriends(userId, friendId);
        eventService.add(Event.of(
                friendId,
                userId,
                EVENT_FRIEND,
                Operation.of(2, "ADD"))
        );
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFromFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFromFriends(userId, friendId);
        eventService.add(Event.of(
                friendId,
                userId,
                EVENT_FRIEND,
                Operation.of(1, "REMOVE"))
        );

    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllUserFriendsByUserId(@PathVariable Long userId) {
        return userService.getAllUserFriendsByUserId(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonUserFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        return userService.getCommonUserFriends(userId, otherId);
    }

    @DeleteMapping("/{userId}")
    public void removeUserById(@PathVariable Long userId) {
        log.debug(String.format("Обработка DELETE запроса по пути /users на удаление пользователя с id=%d", userId));
        userService.removeUserById(userId);
    }

    @GetMapping("/{userId}/feed")
    public Collection<Event> getEventByUserId(@PathVariable Long userId) {
       return eventService.getByUserId(userId);
    }

    @GetMapping("/{userId}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable Long userId) {
        return filmService.getRecommendations(userId);
    }
}
