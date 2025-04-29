package edu.ilkiv.auto_company.exeptions;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends AutoCompanyException {
    public BusinessLogicException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BUSINESS_LOGIC_ERROR");
    }
}