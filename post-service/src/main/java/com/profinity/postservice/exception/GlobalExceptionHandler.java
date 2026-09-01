package com.profinity.postservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(
            IllegalArgumentException ex
    ) {
        log.error("Error: {}", ex.getMessage());
        Map<String,String> erros = new HashMap<>();
        erros.put("error", ex.getMessage());
        erros.put("path", ex.getStackTrace()[0].getClassName());
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<Map<String,String>> handlePostException(
            PostException ex
    ) {
        log.error("Error: {}", ex.getMessage());
        Map<String,String> erros = new HashMap<>();
        erros.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(erros);
    }
}
