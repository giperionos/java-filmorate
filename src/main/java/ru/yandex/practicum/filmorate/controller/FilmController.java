package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Operation;
import ru.yandex.practicum.filmorate.service.event.EventService;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.like.LikeService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final LikeService likeService;
    private final EventService eventService;

    private static final EventType EVENT_LIKE = EventType.of(1, "LIKE");

    @Autowired
    public FilmController(FilmService filmService, LikeService likeService, EventService eventService) {
        this.filmService = filmService;
        this.likeService = likeService;
        this.eventService = eventService;
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
        eventService.add(Event.of(
                id,
                userId,
                EVENT_LIKE,
                Operation.of(2, "ADD"))
        );
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        likeService.removeLike(id, userId);
        eventService.add(Event.of(
                id,
                userId,
                EVENT_LIKE,
                Operation.of(1, "REMOVE"))
        );
    }

    @GetMapping("/popular") //?count={count}
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count,
                                      @RequestParam Optional<Integer> genreId,
                                      @RequestParam Optional<Integer> year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    @DeleteMapping("/{id}")
    public void deleteFilmById(@PathVariable Long id) {
        log.debug(String.format("Обработка DELETE запроса по пути /films на удаление фильма id=%d", id));
        filmService.deleteFilmById(id);
    }


    //GET /films/director/{directorId}?sortBy=[year,likes]
    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilmsForDirector(@PathVariable Integer directorId, @RequestParam String sortBy) {
        return filmService.getSortedFilmsForDirector(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getFilmsByQuery(@RequestParam String query, @RequestParam String by) {
        return filmService.getFilmsByQuery(query, by);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
