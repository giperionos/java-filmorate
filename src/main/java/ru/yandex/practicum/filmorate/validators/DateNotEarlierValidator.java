package ru.yandex.practicum.filmorate.validators;

import ru.yandex.practicum.filmorate.config.FilmorateConfig;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateNotEarlierValidator implements ConstraintValidator<DateNotEarlier, LocalDate> {

    private String startDateString;

    @Override
    public void initialize(DateNotEarlier constraintAnnotation) {
        this.startDateString = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(LocalDate.parse(startDateString, FilmorateConfig.NORMAL_DATE_FORMATTER).minusDays(1));
    }

}
