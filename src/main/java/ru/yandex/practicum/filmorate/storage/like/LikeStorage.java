package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {
    void add(Long filmId, Long userId);
    boolean delete(Long filmId, Long userId);
    List<Like> getAll();
    boolean deleteAll();
}
