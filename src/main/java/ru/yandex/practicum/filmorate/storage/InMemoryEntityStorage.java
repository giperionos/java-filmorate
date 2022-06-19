package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryEntityStorage<T extends Entity> implements Storage<T> {

    private Map<Long, T> entities = new HashMap<>();
    private Long ID = 0L;

    @Override
    public T add(T entity) {

        //получить новый id для сущности
        entity.setId(++ID);

        //добавить в мапу
        entities.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public T update(T entity) {

        //поискать сущность для обновления
        Entity updatedEntity = entities.get(entity.getId());

        if (updatedEntity == null) {
            throw new EntityNotFoundException(String.format("Сущность с %d не найдена в хранилище.", entity.getId()));
        }

        //обновить сущность
        entities.put(entity.getId(), entity);

        return entity;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(entities.values());
    }

    @Override
    public T getById(Long id) {
        return entities.get(id);
    }
}
