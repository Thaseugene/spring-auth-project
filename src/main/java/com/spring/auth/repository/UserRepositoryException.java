package com.spring.auth.repository;

public class UserRepositoryException extends Exception{

    private static final long serialVersionUID = 1L;

    public UserRepositoryException() {
        super();
    }

    public UserRepositoryException(String message) {
        super(message);
    }

    public UserRepositoryException(Exception e) {
        super(e);
    }

    public UserRepositoryException(String message, Exception e) {
        super(message, e);
    }


}
