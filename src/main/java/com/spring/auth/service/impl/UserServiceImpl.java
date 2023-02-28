package com.spring.auth.service.impl;

import com.spring.auth.model.Role;
import com.spring.auth.model.User;
import com.spring.auth.repository.UserRepository;
import com.spring.auth.repository.UserRepositoryException;
import com.spring.auth.service.AlreadyExistsException;
import com.spring.auth.service.UserForm;
import com.spring.auth.service.UserService;
import com.spring.auth.service.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private UserServiceImpl() {
    }


    @Override
    public void addNewUser(UserForm userForm) throws UserServiceException, AlreadyExistsException {
        try {
            if (!userRepository.checkIsLoginExists(userForm.getLogin())) {
                User user = new User(
                        (new Random()).nextInt(),
                        userForm.getName(),
                        userForm.getSurname(),
                        userForm.getEmail(),
                        userForm.getLogin(),
                        userForm.getPassword(),
                        Role.USER,
                        new Date(),
                        true);
                userRepository.addNewUser(user);
            } else {
                throw new AlreadyExistsException("User with this login already exists");
            }
        } catch (UserRepositoryException e) {
            throw new UserServiceException(e);
        }
    }

    @Override
    public User getUserByLoginAndPass(String login, String password) throws UserServiceException, AlreadyExistsException {
        try {
            int id = userRepository.takeUsersIdByLogin(login, password);
            if (id != 0) {
                return userRepository.takeUserById(id);
            } else {
                throw new AlreadyExistsException("Incorrect login or password");
            }
        } catch (UserRepositoryException e) {
            throw new UserServiceException(e);
        }
    }
}
