package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class Operation {
    Integer id;
    String name;
}