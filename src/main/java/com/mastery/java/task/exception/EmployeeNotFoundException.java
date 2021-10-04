package com.mastery.java.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class EmployeeNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Employee not found";

    public EmployeeNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
