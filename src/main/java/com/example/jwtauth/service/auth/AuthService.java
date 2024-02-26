package com.example.jwtauth.service.auth;

import com.example.jwtauth.domain.dto.AuthenticationRequest;
import com.example.jwtauth.domain.dto.AuthenticationResponse;
import com.example.jwtauth.domain.dto.RegistrationRequest;

public interface AuthService {
    AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest);
    AuthenticationResponse refreshToken(String refreshToken);
    void registerUser(RegistrationRequest registrationRequest);

}
