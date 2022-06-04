package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User add(@Valid @RequestBody User user) {

        //установить новый id для нового пользователя
        user.setId(IdSequence.getNewUserId());

        //уточнить имя пользователя - если не задано, установить значением из login
        defineUserName(user);

        //сохранить нового пользователя
        users.put(user.getId(), user);

        //и вернуть его
        return users.get(user.getId());
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {

        //поискать пользователя для обновления
        User updatedUser = users.get(user.getId());

        //если нет такого - ошибка
        if (updatedUser == null) {
            throw new UnknownUserException(String.format("Пользователь с %d не найден!", user.getId()));
        }

        //уточнить имя пользователя - если не задано, установить значением из login
        defineUserName(user);

        //обновить пользователя
        users.put(user.getId(), user);

        //и вернуть его
        return users.get(user.getId());
    }

    @GetMapping
    public List<User> getAll(){
        return new ArrayList<>(users.values());
    }

    //уточнить имя пользователя - если не задано, установить значением из login
    private void defineUserName(User user){
        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
