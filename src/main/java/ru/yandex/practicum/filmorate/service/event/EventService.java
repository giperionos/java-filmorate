package ru.yandex.practicum.filmorate.service.event;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.Collection;

@Service
public class EventService {

    private final EventStorage eventStorage;

    public EventService(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public Collection<Event> getByUserId(Long userId) {
        return eventStorage.getByEventsByUserId(userId);
    }

    public void add(Event event) {
        eventStorage.addNewEvent(event);
    }
}