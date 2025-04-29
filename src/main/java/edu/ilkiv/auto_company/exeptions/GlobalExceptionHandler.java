package edu.ilkiv.auto_company.exeptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AutoCompanyException.class)
  public ResponseEntity<ErrorResponse> handleAutoCompanyException(AutoCompanyException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            ex.getMessage(),
            ex.getErrorCode(),
            ex.getHttpStatus().value()
    );

    return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            "Помилка валідації",
            "VALIDATION_ERROR",
            HttpStatus.BAD_REQUEST.value()
    );

    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
      String errorMessage = error.getDefaultMessage();
      errorResponse.getValidationErrors().add(new ErrorResponse.ValidationError(fieldName, errorMessage));
    });

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            "Внутрішня помилка сервера",
            "INTERNAL_SERVER_ERROR",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}