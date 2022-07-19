package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Storage<T extends Entity> {

    //добавить новую сущность
    T add(T entity);

    //обновить существующую сущность
    T update(T entity);

    //вернуть список всех сущностей
    List<T> getAll();

    //вернуть сущность по id
    T getById(Long id);

    void deleteById(Long id);
}
