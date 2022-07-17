package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.like.LikeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService) {
        this.filmService = filmService;
        this.likeService = likeService;
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        log.debug("Обработка POST запроса по пути /films на добавление фильма.");
        log.debug("Пришел объект: " + film);
        log.debug("Фильм: " + film + "сохранен и передан клиенту.");

        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.debug("Обработка PUT запроса по пути /films на обновление фильма.");
        log.debug("Пришел объект: " + film);
        log.debug("Фильм: " + film + " обновлен и передан клиенту.");

        return filmService.update(film);
    }

    @GetMapping
    public List<Film> getAll(){
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id){
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Long id, @PathVariable Long userId) {
        likeService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        likeService.removeLike(id, userId);
    }

    @GetMapping("/popular") //?count={count}
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopularFilms(count);
    }
}
