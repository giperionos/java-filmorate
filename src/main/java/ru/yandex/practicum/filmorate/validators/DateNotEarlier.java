package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateNotEarlierValidator.class)
@Documented
public @interface DateNotEarlier {
    String message() default "{Date.invalid}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String value();
}
