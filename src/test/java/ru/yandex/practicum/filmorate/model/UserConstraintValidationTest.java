package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.config.FilmorateConfig;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserConstraintValidationTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private Long user_id_null = null;
    private User user;

    @BeforeEach
    public void beforeEach(){
        user = new User(user_id_null, "test@test.ru", "login", "name",
                LocalDate.parse("14.10.1999", FilmorateConfig.NORMAL_DATE_FORMATTER));
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenEmailIsCorrect(){

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldExistValidationErrorsWhenEmailIsNull(){

        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Не указан email пользователя.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenEmailIsNoCorrect(){

        user.setEmail("emal.ru@");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Указанный email пользователя не сооветствует формату email.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenLoginIsCorrect(){

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }

    @Test
    public void shouldExistValidationErrorsWhenLoginIsNull(){

        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Не указан login пользователя.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenLoginContainsWhiteSpaces(){

        user.setLogin("my cool login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Указанный login пользователя содержит пробелы.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenBirthdayDateInFuture(){

        user.setBirthday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Дата рождения пользователя должна быть в прошлом.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldExistValidationErrorsWhenBirthdayDateIsToday(){

        user.setBirthday(LocalDate.now());
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Валидация не содержит ошибки, которые ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
        assertEquals("Дата рождения пользователя должна быть в прошлом.", violations.iterator().next().getMessage());
    }

    @Test
    public void shouldBeEmptyValidationErrorsWhenBirthdayDateInPast(){

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Валидация содержит ошибки, которые не ожидались: " + (violations.isEmpty() ? "" : violations.iterator().next().getMessage()));
    }
}
