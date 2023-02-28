package com.spring.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class GoToErrorPageCommand {

    @RequestMapping("/showError")
    public String showError(Model model) {
        return "error-page";
    }
}
