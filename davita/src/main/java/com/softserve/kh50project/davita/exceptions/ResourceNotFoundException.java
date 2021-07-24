package com.softserve.kh50project.davita.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 78L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
