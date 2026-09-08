package com.profinity.companyservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<Map<String, Object>> companyExceptionHandler(CompanyException ex) {
        log.error("Company Exception occurs : {}", ex.getMessage());
        Map<String , Object> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        errors.put("class", ex.getClass().getName());
        errors.put("status" , HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
