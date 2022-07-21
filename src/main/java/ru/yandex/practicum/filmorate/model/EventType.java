package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class EventType {
    Integer id;
    String name;
}
