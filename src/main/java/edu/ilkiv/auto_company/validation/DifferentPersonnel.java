package edu.ilkiv.auto_company.validation;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import edu.ilkiv.auto_company.dto.RouteSheetDTO;

@Documented
@Constraint(validatedBy = DifferentPersonnelValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentPersonnel {
    String message() default "Водій і кондуктор мають бути різними людьми";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}