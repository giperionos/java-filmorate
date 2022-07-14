package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MPARating {
    private Integer id;
    private String name;
    private String description;

    public MPARating(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public MPARating() {
    }
}
