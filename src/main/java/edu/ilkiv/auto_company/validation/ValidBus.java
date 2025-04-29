package edu.ilkiv.auto_company.validation;

import java.lang.annotation.*;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ValidBusValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBus {
    String message() default "Автобус не може бути використаний, якщо він списаний";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}