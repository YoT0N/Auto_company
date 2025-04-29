package edu.ilkiv.auto_company.exeptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends AutoCompanyException {
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
}