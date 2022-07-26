package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Event {

    private Long eventId;
    private Long timestamp;
    private final Long entityId;
    private final Long userId;
    private final EventType eventType;
    private final Operation operation;
}
