package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends EntityController<User> {

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        log.debug("Обработка POST запроса по пути /users на добавление пользователя.");
        log.debug("Пришел объект: " + user);

        //установить новый id для нового фильма
        user.setId(IdSequence.getNewUserId());

        //и вернуть его
        log.debug("Пользователь: " + user + "сохранен и передан клиенту.");
        return super.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.debug("Обработка PUT запроса по пути /users на обновление фильма.");
        log.debug("Пришел объект: " + user);

        //поискать фильм для обновления
        User updatedUser;
        try {
            updatedUser = super.update(user);
        } catch (RuntimeException e) {
            log.debug("Ошибка поиска фильма.");
            throw new UnknownUserException(String.format("Фильм с %d не найден!", user.getId()));
        }

        //и вернуть его
        log.debug("Фильм: " + user + " обновлен и передан клиенту.");
        return updatedUser;
    }

    @GetMapping
    public List<User> getAll(){
        return super.getAll(User.class.getName());
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
