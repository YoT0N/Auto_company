package edu.ilkiv.auto_company.exeptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends AutoCompanyException {
    public DuplicateResourceException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s з %s '%s' вже існує", resourceName, fieldName, fieldValue),
                HttpStatus.CONFLICT, "DUPLICATE_RESOURCE");
    }
}