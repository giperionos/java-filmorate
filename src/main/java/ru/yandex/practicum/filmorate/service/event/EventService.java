package ru.yandex.practicum.filmorate.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventService {

    private final EventStorage eventStorage;

    public Collection<Event> getByUserId(Long userId) {
        return eventStorage.getByEventsByUserId(userId);
    }

    public void add(Event event) {
        eventStorage.addNewEvent(event);
    }
}