package edu.ilkiv.auto_company.validation;

import java.time.LocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// Валідатор для перевірки віку
class AgeValidatorForDriver implements ConstraintValidator<ValidDriverAge, LocalDate> {
    private int minAge;

    @Override
    public void initialize(ValidDriverAge constraintAnnotation) {
        this.minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }
        return birthDate.plusYears(minAge).isBefore(LocalDate.now());
    }
}