package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateNotEarlierValidator implements ConstraintValidator<DateNotEarlier, LocalDate> {

    private String startDateString;

    @Override
    public void initialize(DateNotEarlier constraintAnnotation) {
        this.startDateString = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(LocalDate.parse(startDateString, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }

}
