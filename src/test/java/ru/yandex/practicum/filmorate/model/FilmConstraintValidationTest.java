package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmConstraintValidationTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Long film1_id = null;
    private String film1_name = "Matrix";
    private String film1_description = "Super-Mega Film";
    private LocalDate film1_releaseDate = LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER);
    private Integer film1_duration = 136;
    private MpaRating film1_mpa = new MpaRating(4, "R", "Лицам до 17 лет просматривать фильм можно только в присутствии взрослого" );
    private Set<Genre> film1_genres = null;
    private Set<Director> film1_directors = null;
    private Film film = new Film(film1_id, film1_name, film1_description, film1_releaseDate, film1_duration, film1_mpa,
            film1_genres, film1_directors);

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

    @Test
    public void shouldExistValidationErrorsWhenReleaseDateEarlierThen28121895(){

        film.setReleaseDate(LocalDate.parse("27.12.1895", FilmorateConfig.NORMAL_DATE_FORMATTER));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Дата релиза должна быть не ранее чем 28.12.1895.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenReleaseDateEquals28121895(){

        film.setReleaseDate(LocalDate.parse("28.12.1895", FilmorateConfig.NORMAL_DATE_FORMATTER));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenReleaseDateMoreThen28121895(){

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

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
