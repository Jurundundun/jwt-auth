package com.example.jwtauth.controller;

import com.example.jwtauth.domain.dto.AuthenticationRequest;
import com.example.jwtauth.domain.dto.AuthenticationResponse;
import com.example.jwtauth.domain.dto.RegistrationRequest;
import com.example.jwtauth.service.AuthService;
import com.example.jwtauth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/auth")
    public AuthenticationResponse authenticateUser(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return authService.authenticateUser(authenticationRequest);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshToken(@RequestParam String refreshToken) {
        return authService.refreshToken(refreshToken);
    }
    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        authService.registerUser(registrationRequest);
    }
    @GetMapping("/me")
    public String getLoggedInUsername() {
        return userService.getUsernameFromAuthorizedUser();
    }
    @GetMapping("/user")
    public List<String> getAllUsernames() {
        return userService.getAllUsernames();
    }
    @GetMapping("/user-only-access")
    public String getMessageForUser() {
        return "Авторизован";
    }
}
