package com.spring.auth.controller;

import com.spring.auth.service.AlreadyExistsException;
import com.spring.auth.service.UserForm;
import com.spring.auth.service.UserService;
import com.spring.auth.service.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/reg")
public class RegistrationProcessCommand {

    public static final String PRESENTATION = "presentation";
    public static final String REGISTRATION = "registration";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String REDIRECT_REG_SHOW_REG = "redirect:/reg/showReg";
    public static final String REDIRECT_ERROR_SHOW_ERROR = "redirect:/error/showError";
    public static final String SOME_FIELDS_ARE_EMPTY = "Some fields are empty";
    public static final String PASSWORD_ARE_NOT_EQUAL = "Password are not equal";
    public static final String ACCOUNT_SUCCESSFULLY_CREATED = "Account successfully created";
    public static final String BASE_VIEW = "base-view";
    public static final String USER_FORM = "userForm";
    @Autowired
    private UserService userService;


    @RequestMapping("/showReg")
    public String showRegForm(Model model) {
        model.addAttribute(PRESENTATION, REGISTRATION);
        model.addAttribute(USER_FORM, new UserForm());
        return BASE_VIEW;
    }

    @RequestMapping("/processForm")
    public String processForm(@ModelAttribute("userForm") UserForm userForm, Model model) {
        try {
            if (validateUserForm(userForm)) {
                if (confirmPassword(userForm)) {
                    userService.addNewUser(userForm);
                    model.addAttribute(MESSAGE, ACCOUNT_SUCCESSFULLY_CREATED);
                } else {
                    model.addAttribute(ERROR, PASSWORD_ARE_NOT_EQUAL);
                }
            } else {
                model.addAttribute(ERROR, SOME_FIELDS_ARE_EMPTY);
            }
            return REDIRECT_REG_SHOW_REG;
        } catch (UserServiceException e) {
            model.addAttribute(ERROR, e.getMessage());
            return REDIRECT_ERROR_SHOW_ERROR;
        } catch (AlreadyExistsException e) {
            model.addAttribute(ERROR, e.getMessage());
            return REDIRECT_REG_SHOW_REG;
        }
    }

    private boolean validateUserForm(UserForm userForm) {
        if (userForm.getName() == null ||
                userForm.getSurname() == null ||
                userForm.getEmail() == null ||
                userForm.getLogin() == null ||
                userForm.getPassword() == null ||
                userForm.getConfirmPassword() == null) {
            return false;
        } else return !userForm.getName().trim().isEmpty() &&
                !userForm.getSurname().trim().isEmpty() &&
                !userForm.getEmail().trim().isEmpty() &&
                !userForm.getLogin().trim().isEmpty() &&
                !userForm.getPassword().trim().isEmpty() &&
                !userForm.getConfirmPassword().trim().isEmpty();
    }

    private boolean confirmPassword(UserForm userForm) {
        return Objects.equals(userForm.getPassword(), userForm.getConfirmPassword());
    }

}
