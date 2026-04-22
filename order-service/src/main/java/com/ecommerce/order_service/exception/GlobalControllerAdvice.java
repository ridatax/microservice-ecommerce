package com.ecommerce.order_service.exception;

import java.time.Instant;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.warn("[handleResourceNotFoundException] Resource not found: - Path: [{}], Message: [{}]",
                request.getDescription(Boolean.FALSE), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setProperty("TimeStamp", Instant.now().toString());
        problemDetail.setProperty("ResourceName", ex.getResourceName());
        problemDetail.setProperty("Field", ex.getFieldName());
        problemDetail.setProperty("Value", ex.getFieldValue());
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("TimeStamp", Instant.now().toString());
        problemDetail.setProperty("Errors", ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toCollection(java.util.ArrayList::new)));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request) {
        log.error("[handleException] Unhandled exception: - Path: [{}], Message: [{}]",
                request.getDescription(Boolean.FALSE), ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty("TimeStamp", Instant.now().toString());
        return problemDetail;
    }
}
