package edu.ilkiv.auto_company.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import edu.ilkiv.auto_company.dto.BusDTO;
import java.time.LocalDate;

class ValidBusValidator implements ConstraintValidator<ValidBus, BusDTO> {
    @Override
    public boolean isValid(BusDTO bus, ConstraintValidatorContext context) {
        if (bus == null) {
            return true;
        }

        // Якщо автобус має дату списання і ця дата в минулому - не валідний
        return bus.getWriteoffDate() == null || bus.getWriteoffDate().isAfter(LocalDate.now());
    }
}