package com.spring.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedirectToStartPageCommand {

    @RequestMapping("/")
    public String showLoginPage() {
        return "redirect:auth/showAuth";
    }
}
