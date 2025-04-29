package edu.ilkiv.auto_company.exeptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AutoCompanyException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public AutoCompanyException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}