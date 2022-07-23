package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
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
    public Film addNewFilm(@Valid @RequestBody Film film) {
        log.debug("Обработка POST запроса по пути /films на добавление фильма.");
        log.debug("Пришел объект: " + film);
        log.debug("Фильм: " + film + "сохранен и передан клиенту.");

        return filmService.addNewFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("Обработка PUT запроса по пути /films на обновление фильма.");
        log.debug("Пришел объект: " + film);
        log.debug("Фильм: " + film + " обновлен и передан клиенту.");

        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms(){
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId){
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeForFilmIdByUserId(@PathVariable Long filmId, @PathVariable Long userId) {
        likeService.addLike(filmId, userId);
        eventService.add(Event.of(
                filmId,
                userId,
                EVENT_LIKE,
                Operation.of(2, "ADD"))
        );
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLikeFilmIdByUserId(@PathVariable Long filmId, @PathVariable Long userId) {
        likeService.removeLike(filmId, userId);
        eventService.add(Event.of(
                filmId,
                userId,
                EVENT_LIKE,
                Operation.of(1, "REMOVE"))
        );
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count,
                                      @RequestParam Optional<Integer> genreId,
                                      @RequestParam Optional<Integer> year) {
        return filmService.getPopularFilms(count, genreId, year);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable Long filmId) {
        log.debug(String.format("Обработка DELETE запроса по пути /films на удаление фильма с id=%d", filmId));
        filmService.deleteFilmById(filmId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilmsForDirector(@PathVariable Integer directorId,
                                                @RequestParam(name = "sortBy") String sortByTypeStr) {

        return filmService.getSortedFilmsForDirector(directorId, SortType.valueOf(sortByTypeStr.toUpperCase()));
    }

    @GetMapping("/search")
    public List<Film> getFilmsByQuery(@RequestParam String query, @RequestParam(name = "by") String queryParam) {
        return filmService.getFilmsByQuery(query, queryParam);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
