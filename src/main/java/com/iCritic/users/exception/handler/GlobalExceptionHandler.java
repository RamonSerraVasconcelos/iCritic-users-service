package com.iCritic.users.exception.handler;

import com.iCritic.users.exception.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

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

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorDetails> unauthorizedAccessExceptionHandler(UnauthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ErrorDetails> forbiddenAccessExceptionHandler(ForbiddenAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorDetails> connectExceptionHandler(ConnectException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ErrorDetails> fileSizeLimitExceededExceptionHandler(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError("File size limit exceeded"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ErrorDetails> multipartExceptionHandler(MultipartException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetails> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildResponseError("Invalid request data"));
    }
}
