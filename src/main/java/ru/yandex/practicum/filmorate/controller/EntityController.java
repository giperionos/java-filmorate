package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UnknownFilmException;
import ru.yandex.practicum.filmorate.exceptions.UnknownUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.util.IdSequence;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
public abstract class EntityController<T extends Entity> {

    private Map<String, Map<Long, T>> entities = new HashMap<>();


    @PostMapping
    public T add(@Valid @RequestBody T entity) {
        log.debug("Обработка POST запроса на добавление " + entity.getEntityName());

        //пришел объект на добавление
        log.debug("Пришел объект: " + entity);

        //установить новый id для новой сущности
        switch (entity.getEntityName()){
            case FILM:
                entity.setId(IdSequence.getNewFilmId());
                break;

            case USER:
                entity.setId(IdSequence.getNewUserId());
                break;

            default:
                throw new RuntimeException("Не известный тип сущности: " + entity);
        }

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

        log.debug(entity.getEntityName()+ " " + entity + "сохранен и передан клиенту.");
        return entities.get(entity.getClass().getName()).get(entity.getId());
    }

    @PutMapping
    public T update(@Valid @RequestBody T entity) {
        log.debug("Обработка PUT запроса на обновление " + entity.getEntityName());
        log.debug("Пришел объект: " + entity);

        //поискать пользователя для обновления
        Entity updatedEntity = entities.get(entity.getClass().getName()).get(entity.getId());

        if (updatedEntity == null) {
            log.debug("Ошибка поиска " + entity.getEntityName() + ".");
            switch (entity.getEntityName()){
                case FILM:
                    throw new UnknownFilmException(String.format("Фильм с %d не найден!", entity.getId()));

                case USER:
                    throw new UnknownUserException(String.format("Пользователь с %d не найден!", entity.getId()));

                default:
                    throw new RuntimeException("Cущность вида " + entity + " не найдена.");
            }
        }

        //обновить сущность
        entities.get(entity.getClass().getName()).put(entity.getId(), entity);

        log.debug(entity.getEntityName() + " " + entity + "сохранен и передан клиенту.");
        return entities.get(entity.getClass().getName()).get(entity.getId());
    }


    public List<T> getAll(String entityClassName){
        return new ArrayList<>(entities.get(entityClassName).values());
    }

    @ExceptionHandler({UnknownFilmException.class, UnknownUserException.class})
    public void handleUnknownEntityException(RuntimeException e) {
        log.warn(e.getMessage());
        throw e;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        throw new ValidationException(e.getMessage());
    }
}
