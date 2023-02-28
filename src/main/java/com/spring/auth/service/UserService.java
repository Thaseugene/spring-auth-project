package com.spring.auth.service;

import com.spring.auth.model.User;

public interface UserService {

    void addNewUser(UserForm userForm) throws UserServiceException, AlreadyExistsException;

    User getUserByLoginAndPass(String login, String password) throws UserServiceException, AlreadyExistsException;

}
