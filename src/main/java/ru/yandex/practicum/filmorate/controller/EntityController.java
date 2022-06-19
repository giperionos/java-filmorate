package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public abstract class EntityController<T extends Entity> {

    private Map<String, Map<Long, T>> entities = new HashMap<>();

    public T add(T entity) {

        //мапа для текущей сущности
        Map<Long, T> entityMap;

        //сначала нужно проверить, есть ли вообще мапа для текущей сущности
        entityMap = entities.get(entity.getClass().getName());

        if (entityMap == null) {
            //если нет, то создать новую
            entityMap = new HashMap<>();
        }

        //сохранить сущность в ее мапу
        entityMap.put(entity.getId(), entity);

        //сохранить мапу сущностей этого типа в общую мапу
        entities.put(entity.getClass().getName(), entityMap);

        return entities.get(entity.getClass().getName()).get(entity.getId());
    }


    public T update(T entity) {

        //поискать пользователя для обновления
        Entity updatedEntity = entities.get(entity.getClass().getName()).get(entity.getId());

        if (updatedEntity == null) {
            throw new RuntimeException("В мапе нет сущности для обновления.");
        }

        //обновить сущность
        entities.get(entity.getClass().getName()).put(entity.getId(), entity);

        return entities.get(entity.getClass().getName()).get(entity.getId());
    }


    public List<T> getAll(String entityClassName){
        return new ArrayList<>(entities.get(entityClassName).values());
    }

}
