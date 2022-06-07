package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Entity {
    protected Long id;

    public EntityType getEntityName(){
        return EntityType.COMMON;
    }
}
