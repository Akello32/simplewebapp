package com.mastery.java.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DepartmentNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Department not found";

    public DepartmentNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
