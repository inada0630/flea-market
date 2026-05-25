package com.example.fleamarket.controller;

import com.example.fleamarket.form.UserForm;
import com.example.fleamarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegister(@ModelAttribute("userRegisterForm") UserForm form) {
        return "register";
    }

    @PostMapping("/register")
    public String register(@Validated @ModelAttribute("userRegisterForm") UserForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.register(form);
        return "redirect:/";
    }
}