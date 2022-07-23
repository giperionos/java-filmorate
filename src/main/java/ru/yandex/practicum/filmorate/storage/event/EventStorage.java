package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.Collection;

public interface EventStorage {
    Collection<Event> getByEventsByUserId(Long userId);

    void addNewEvent(Event event);
}