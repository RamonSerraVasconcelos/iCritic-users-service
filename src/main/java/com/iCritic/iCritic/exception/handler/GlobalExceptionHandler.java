package com.iCritic.iCritic.exception.handler;

import com.iCritic.iCritic.exception.ResourceConflictException;
import com.iCritic.iCritic.exception.ResourceNotFoundException;
import com.iCritic.iCritic.exception.ResourceViolationException;
import com.iCritic.iCritic.exception.ForbiddenAccessException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.ConnectException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ErrorDetails buildResponseError(String message) {
        return ErrorDetails.builder()
                .message(message)
                .build();
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ErrorDetails> resourceConflictExceptionHandler(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(buildResponseError(ex.getMessage()));
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(ResourceViolationException.class)
    public ResponseEntity<ErrorDetails> resourceViolationExceptionHandler(ResourceViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorDetails> forbiddenAccessExceptionHandler(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDetails> nullPointerExceptionHandler(NullPointerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorDetails> connectExceptionHandler(ConnectException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
