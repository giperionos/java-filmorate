package ru.yandex.practicum.filmorate.storage.film.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryEntityStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryEntityStorage<Film> implements FilmStorage {
    @Override
    public List<Film> getMostPopularList(Long count) {
        return super.getAll().stream()
                .sorted((f1, f2) ->{
                    int result;
                    result = f1.getLikes().size() - f2.getLikes().size();
                    return -1 * result;
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
