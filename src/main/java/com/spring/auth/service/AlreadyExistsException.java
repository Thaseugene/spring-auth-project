package com.spring.auth.service;

public class AlreadyExistsException extends Exception{

    private static final long serialVersionUID = 1L;

    public AlreadyExistsException() {
        super();
    }

    public AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(Exception e) {
        super(e);
    }

    public AlreadyExistsException(String message, Exception e) {
        super(message, e);
    }
}
