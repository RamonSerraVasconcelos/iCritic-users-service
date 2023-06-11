package com.iCritic.iCritic.exception;

import javax.validation.ConstraintViolation;
import java.util.Set;
import java.util.stream.Collectors;

public class ResourceViolationException extends RuntimeException {
    public ResourceViolationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(toString(constraintViolations));
    }

    public ResourceViolationException(ConstraintViolation violation) {
        super(violation.getMessage());
    }

    public ResourceViolationException(String message) {
        super(message);
    }

    private static String toString(Set<? extends ConstraintViolation<?>> constraintViolations) {
        return (String) constraintViolations.stream().map((cv) -> {
            return cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage();
        }).collect(Collectors.joining(", "));
    }
}
