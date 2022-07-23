package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmConstraintValidationTest {

    private static Validator validator;

    private Film film;

    @BeforeAll
    public static void beforeAll(){
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @BeforeEach
    public void beforeEach(){
        film = new Film(
                null,
                "Matrix",
                "Super-Mega Film",
                LocalDate.parse("14.10.1999", FilmorateConfig.normalDateFormatter),
                136,
                new MPARating(1, "G", "У фильма нет возрастных ограничений" ),
                null,
                null);
    }

    //Test Name

    @Test
    public void shouldBeEmptyValidationErrorsWhenNameIsNotNull(){

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldExistValidationErrorsWhenNameIsNull(){

        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Название фильма не должно быть пустым.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenNameWithWhiteSpaces(){

        film.setName("  ");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Название фильма не должно быть пустым.", violations.iterator().next().getMessage());
    }

    //Test Description

    @Test
    public void shouldBeEmptyValidationErrorsWhenDescriptionIsEmpty(){

        film.setDescription("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenDescriptionIsNull(){

        film.setDescription(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }


    @Test
    public void shouldBeEmptyValidationErrorsWhenDescriptionExistsAndLengthLessThen200(){

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }


    @Test
    public void shouldBeEmptyValidationErrorsWhenDescriptionExistsAndLengthEqual200(){

        film.setDescription("m".repeat(200));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }


    @Test
    public void shouldExistValidationErrorsWhenDescriptionExistsAndLengthMoreThen200(){

        film.setDescription("m".repeat(201));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Описание фильма должно быть менее 200 символов.", violations.iterator().next().getMessage());
    }


    //Test releaseDate

    @Test
    public void shouldExistValidationErrorsWhenReleaseDateEarlierThen28121895(){

        film.setReleaseDate(LocalDate.parse("27.12.1895", FilmorateConfig.normalDateFormatter));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Дата релиза должна быть не ранее чем 28.12.1895.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenReleaseDateEquals28121895(){

        film.setReleaseDate(LocalDate.parse("28.12.1895", FilmorateConfig.normalDateFormatter));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenReleaseDateMoreThen28121895(){

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    //Test duration

    @Test
    public void shouldExistValidationErrorsWhenDurationLessThenZero(){

        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Продолжительность фильма должна быть положительной.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenDurationEqualsZero(){

        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Продолжительность фильма должна быть положительной.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenDurationMoreThenZero(){

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }


}