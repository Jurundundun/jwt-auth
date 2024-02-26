package com.example.jwtauth.controller;

import com.example.jwtauth.domain.dto.AuthenticationRequest;
import com.example.jwtauth.domain.dto.AuthenticationResponse;
import com.example.jwtauth.domain.dto.RegistrationRequest;
import com.example.jwtauth.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
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

}
