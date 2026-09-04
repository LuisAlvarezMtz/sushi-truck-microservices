package com.luisalvarez.inventoryservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest){
        log.warn("Resource not found - path {}, Message: {}"
                , webRequest.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("Resource not found");
        problemDetail.setType(URI.create("https://sushitruck.api/errors/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("resource", ex.getResourceName());
        problemDetail.setProperty("field", ex.getFieldName());
        problemDetail.setProperty("value", ex.getFieldValue());

        return problemDetail;
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleResourceAlreadyExists(ResourceAlreadyExistsException ex, WebRequest webRequest){
        log.warn("Resource conflict - path {}, Message: {}", webRequest.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problemDetail.setTitle("Resource already exists");
        problemDetail.setType(URI.create("https://sushitruck.api/errors/conflict"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields");

        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://sushitruck.api/errors/validation"));
        problemDetail.setProperty("timestamp", Instant.now());

        Map<String, String> errorMap = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMap.put(error.getField(), error.getDefaultMessage())
                );
        problemDetail.setProperty("errors", errorMap);

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest webRequest){
        log.error("An unexpected error has occurred {}, Message: {}"
                , webRequest.getDescription(false), ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error has occurred. Please contact the administrator");

        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://sushitruck.api/errors/internal"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest webRequest){
        log.warn("Data integrity violation - path {}, Message: {}", webRequest.getDescription(false), ex.getMessage());

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "The resource you're trying to create already exists");
        problemDetail.setTitle("Data integrity violation");
        problemDetail.setType(URI.create("https://sushitruck.api/errors/conflict"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
