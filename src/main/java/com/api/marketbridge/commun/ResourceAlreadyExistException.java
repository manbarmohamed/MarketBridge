package com.api.marketbridge.commun;

public class ResourceAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistException(String message) {
        super(message);
    }

    public ResourceAlreadyExistException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
