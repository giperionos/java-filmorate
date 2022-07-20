package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Director {

    private Integer id;

    @NotBlank
    private String name;

    public Director(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
