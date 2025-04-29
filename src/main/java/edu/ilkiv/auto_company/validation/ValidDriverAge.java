package edu.ilkiv.auto_company.validation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Анотація для перевірки мінімального віку водія
@Documented
@Constraint(validatedBy = AgeValidatorForDriver.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDriverAge {
    String message() default "Водій має бути старшим 21 року";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int minAge() default 21;
}