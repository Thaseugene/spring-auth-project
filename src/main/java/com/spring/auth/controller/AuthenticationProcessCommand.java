package com.spring.auth.controller;

import com.spring.auth.model.User;
import com.spring.auth.service.AlreadyExistsException;
import com.spring.auth.service.UserService;
import com.spring.auth.service.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthenticationProcessCommand {

    @Autowired
    private UserService userService;

    @RequestMapping("/showAuth")
    public String showAuthForm(Model model) {
        model.addAttribute("presentation", "authentication");
        return "base-view";
    }

    @RequestMapping("/processForm")
    public String processForm(@RequestParam("login") String login, @RequestParam("password") String password, Model model, HttpSession session) {
        try {
            if (login == null || password == null || login.trim().isEmpty() || password.trim().isEmpty()) {
                model.addAttribute("presentation", "authentication");
                model.addAttribute("error", "Some fields are empty");
                return "redirect:/auth/showAuth";
            } else {
                User user = userService.getUserByLoginAndPass(login, password);
                model.addAttribute("presentation", "userView");
                setSessionAttributes(user, session);
                return "base-view";
            }
        } catch (UserServiceException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/error/showError";
        } catch (AlreadyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/auth/showAuth";
        }
    }

    private void setSessionAttributes(User user, HttpSession session) {
        session.setAttribute("name", user.getName());
        session.setAttribute("surname", user.getSurname());
        session.setAttribute("role", user.getRole());
        session.setAttribute("email", user.getEmail());
    }

}
