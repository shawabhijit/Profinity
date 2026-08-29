package com.profinity.userservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String , String>> handleValidationExceptions(
            IllegalArgumentException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("cause", ex.getCause().toString());
        log.error("Validation Exception: {}", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, String>> handleUserException(UserException e) {
        log.error("User Exception: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message" , e.getMessage());
        errors.put("cause" , e.getCause().toString() );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectionException.class)
    public ResponseEntity<Map<String, String>> handleConnectionException(ConnectionException e) {
        log.error("Connection Exception: {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        errors.put("message" , e.getMessage());
        errors.put("cause" , e.getCause().toString() );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
