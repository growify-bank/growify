package org.growify.bank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MESSAGE_KEY = "message";

    @ExceptionHandler(TokenGenerationException.class)
    public ResponseEntity<Object> handleTokenGenerationException(TokenGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Object> handleTokenInvalidException(TokenInvalidException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap(MESSAGE_KEY, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            if (!errors.containsKey(fieldName)) {
                errors.put(fieldName, errorMessage);
            }
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
