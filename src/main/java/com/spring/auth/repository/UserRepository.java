package com.spring.auth.repository;

import com.spring.auth.model.User;

public interface UserRepository {
    int takeUsersIdByLogin(String login, String password) throws UserRepositoryException;
    User takeUserById(int id) throws UserRepositoryException;
    void addNewUser(User user) throws UserRepositoryException;

    boolean checkIsLoginExists(String login) throws UserRepositoryException;
}
