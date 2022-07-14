package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.List;

public interface LikesStorage {
    void add(Long filmId, Long userId);
    boolean delete(Long filmId, Long userId);
    List<Likes> getAll();
    boolean deleteAll();
}
