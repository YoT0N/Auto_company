package edu.ilkiv.auto_company.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import edu.ilkiv.auto_company.dto.RouteSheetDTO;


class DifferentPersonnelValidator implements ConstraintValidator<DifferentPersonnel, RouteSheetDTO> {
    @Override
    public boolean isValid(RouteSheetDTO routeSheet, ConstraintValidatorContext context) {
        if (routeSheet == null || routeSheet.getDriverTabelNumber() == null || routeSheet.getConductorTabelNumber() == null) {
            return true;
        }

        return !routeSheet.getDriverTabelNumber().equals(routeSheet.getConductorTabelNumber());
    }
}