package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.debug("Обработка POST запроса по пути /users на добавление пользователя.");
        log.debug("Пришел объект: " + user);

        //установить новый id для нового пользователя
        user.setId(IdSequence.getNewUserId());

        //уточнить имя пользователя - если не задано, установить значением из login
        defineUserName(user);

        //сохранить нового пользователя
        users.put(user.getId(), user);

        //и вернуть его
        log.debug("Пользователь: " + user + "сохранен и передан клиенту.");
        return users.get(user.getId());
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обработка PUT запроса по пути /users на обновление пользователя.");
        log.debug("Пришел объект: " + user);

        //поискать пользователя для обновления
        User updatedUser = users.get(user.getId());

        //если нет такого - ошибка
        if (updatedUser == null) {
            log.debug("Ошибка поиска пользователя.");
            throw new UnknownUserException(String.format("Пользователь с %d не найден!", user.getId()));
        }

        //уточнить имя пользователя - если не задано, установить значением из login
        defineUserName(user);

        //обновить пользователя
        users.put(user.getId(), user);

        //и вернуть его
        log.debug("Пользователь: " + user + "обновлен и передан клиенту.");
        return users.get(user.getId());
    }

    @GetMapping
    public List<User> getAll(){
        return new ArrayList<>(users.values());
    }

    //уточнить имя пользователя - если не задано, установить значением из login
    private void defineUserName(User user){
        log.trace("Проверка имени пользователя с id = " + user.getId());
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.trace("У пользователя не задано имя.");
            user.setName(user.getLogin());
            log.trace("Пользователю с id = " + user.getId() + " присвоено имя: " + user.getName());
        }
    }

    @ExceptionHandler(UnknownUserException.class)
    public void handleUnknownUserException(UnknownUserException e) {
        log.warn(e.getMessage());
        throw e;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        throw new ValidationException(e.getMessage());
    }
}
