package com.example.jwtauth.controller;

import com.example.jwtauth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/me")
    public String getLoggedInUsername() {
        return userService.getUsernameFromAuthorizedUser();
    }
    @GetMapping
    public List<String> getAllUsernames() {
        return userService.getAllUsernames();
    }
    @GetMapping("/only-access")
    public String getMessageForUser() {
        return "Авторизован";
    }
}
