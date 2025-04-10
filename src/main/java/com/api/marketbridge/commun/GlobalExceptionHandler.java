package com.api.marketbridge.commun;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExistException(
            ResourceAlreadyExistException ex) {
        ApiError error = new ApiError();
        error.setMessage("Resource already exists");
        error.setDetails(ex.getMessage());
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        ApiError error = new ApiError();
        error.setMessage("Validation failed");
        error.setDetails(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.status(400).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError error = new ApiError();
        error.setMessage("An unexpected error occurred");
        error.setDetails(ex.getMessage());
        return ResponseEntity.status(500).body(error);
    }
}