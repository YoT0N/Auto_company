package edu.ilkiv.auto_company.exeptions;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String message;
    private String errorCode;
    private int status;
    private List<ValidationError> validationErrors = new ArrayList<>();

    public ErrorResponse(LocalDateTime timestamp, String message, String errorCode, int status) {
        this.timestamp = timestamp;
        this.message = message;
        this.errorCode = errorCode;
        this.status = status;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
    }
}