package com.example.ecommerce_api.exception;

import com.example.ecommerce_api.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        GenericResponse response = new GenericResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GenericResponse> handleIllegalStateException(IllegalStateException ex) {
        // Create a response body with the message from the exception
        GenericResponse response = new GenericResponse(ex.getMessage());
        // Return an HTTP 409 Conflict status
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
