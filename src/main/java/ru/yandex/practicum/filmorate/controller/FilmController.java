package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.debug("Обработка POST запроса по пути /films на добавление фильма.");
        log.debug("Пришел объект: " + film);

        //установить новый id для нового фильма
        film.setId(IdSequence.getNewFilmId());
        log.debug("Фильму присвоен id = " + film.getId());

        //сохранить новый фильм
        films.put(film.getId(), film);
        log.debug("Фильм c id = " + film.getId() + " сохранен.");

        //и вернуть его
        log.debug("Фильм: " + film + " передан клиенту.");
        return films.get(film.getId());
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обработка PUT запроса по пути /films на обновление фильма.");
        log.debug("Пришел объект: " + film);

        //поискать фильм для обновления
        log.debug("Поиск фильма с id = " + film.getId() + " в памяти приложения.");
        Film updatedFilm = films.get(film.getId());

        //если нет такого - ошибка
        if (updatedFilm == null) {
            log.debug("Ошибка поиска фильма.");
            throw new UnknownFilmException(String.format("Фильм с %d не найден!", film.getId()));
        }

        //обновить фильм
        films.put(film.getId(), film);
        log.debug("Фильм c id = " + film.getId() + " обновлен.");

        //и вернуть его
        log.debug("Фильм: " + film + " передан клиенту.");
        return films.get(film.getId());
    }

    @GetMapping
    public List<Film> getAll(){
        return new ArrayList<>(films.values());
    }
}
