package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {

        //установить новый id для нового фильма
        film.setId(IdSequence.getNewId());

        //сохранить новый фильм и вернуть его
        return films.put(film.getId(), film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {

        //поискать фильм для обновления
        Film updatedFilm = films.get(film.getId());

        //если нет такого - ошибка
        if (updatedFilm == null) {
            throw new UnknownFilmException(String.format("Фильм с %d не найден!", film.getId()));
        }

        //обновить фильм и вернуть его
        return films.put(film.getId(), film);
    }

    @GetMapping
    public List<Film> getAll(){
        return new ArrayList<>(films.values());
    }
}
