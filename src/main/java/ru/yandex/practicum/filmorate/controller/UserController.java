package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController extends EntityController<User> {

    @GetMapping
    public List<User> getAll(){
        return super.getAll(User.class.getName());
    }

}
