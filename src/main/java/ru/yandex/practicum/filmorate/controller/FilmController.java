package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends EntityController<Film> {

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.debug("Обработка POST запроса по пути /films на добавление фильма.");
        log.debug("Пришел объект: " + film);

        //установить новый id для нового фильма
        film.setId(IdSequence.getNewFilmId());

        //и вернуть его
        log.debug("Фильм: " + film + "сохранен и передан клиенту.");
        return super.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обработка PUT запроса по пути /films на обновление фильма.");
        log.debug("Пришел объект: " + film);

        //поискать фильм для обновления
        Film updatedFilm;
        try {
            updatedFilm = super.update(film);
        } catch (RuntimeException e) {
            log.debug("Ошибка поиска фильма.");
            throw new UnknownFilmException(String.format("Фильм с %d не найден!", film.getId()));
        }

        //и вернуть его
        log.debug("Фильм: " + film + " обновлен и передан клиенту.");
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAll(){
        return super.getAll(Film.class.getName());
    }

    @ExceptionHandler(UnknownFilmException.class)
    public void handleUnknownFilmException(UnknownFilmException e) {
        log.warn(e.getMessage());
        throw e;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        throw new ValidationException(e.getMessage());
    }
}
