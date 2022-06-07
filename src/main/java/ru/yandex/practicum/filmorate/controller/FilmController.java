package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends EntityController<Film> {

    @GetMapping
    public List<Film> getAll(){
        return super.getAll(Film.class.getName());
    }
}
