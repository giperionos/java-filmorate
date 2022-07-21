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
        eventService.add(Event.of(
                friendId,
                userId,
                EVENT_FRIEND,
                Operation.of(2, "ADD"))
        );
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        userService.removeFromFriends(userId, friendId);
        eventService.add(Event.of(
                friendId,
                userId,
                EVENT_FRIEND,
                Operation.of(1, "REMOVE"))
        );

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

    @GetMapping("/{id}/feed")
    public Collection<Event> getEventByUserId(@PathVariable Long id) {
       return eventService.getByUserId(id);
    }

    @GetMapping("/{id}/recommendations")
    public Collection<Film> getRecommendations(@PathVariable Long id) {
        return filmService.getRecommendations(id);
    }
}
