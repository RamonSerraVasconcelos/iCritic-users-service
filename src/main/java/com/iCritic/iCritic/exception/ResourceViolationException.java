package com.iCritic.iCritic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceViolationException extends RuntimeException {
    public ResourceViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(toString(constraintViolations));
    }

    public ResourceViolationException(ConstraintViolation violation) {
        super(violation.getMessage());
    }

    private static String toString(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return (String)constraintViolations.stream().map((cv) -> {
            return cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage();
        }).collect(Collectors.joining(", "));
    }
}
