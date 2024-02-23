package com.example.jwtauth.service;

import com.example.jwtauth.domain.dto.AuthenticationRequest;
import com.example.jwtauth.domain.dto.AuthenticationResponse;
import com.example.jwtauth.domain.dto.RegistrationRequest;
import com.example.jwtauth.domain.entity.User;
import com.example.jwtauth.domain.enumm.Role;
import com.example.jwtauth.error.CredentialsAlreadyExistsException;
import com.example.jwtauth.error.NotValidTokenException;
import com.example.jwtauth.util.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public AuthenticationResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        User user = userService.findByUsername(authenticationRequest.getUsername());
        // Расшифровываем пароль и сравниваем его с введенным пользователем
        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            // Пароли не совпадают
            throw new BadCredentialsException("Неверный пароль");
        }

        UserDetails userDetails = userService.userDetailsService().loadUserByUsername(
                authenticationRequest.getUsername());
        return new AuthenticationResponse(jwtTokenUtil.generateRefreshToken(userDetails),
                jwtTokenUtil.generateAccessToken(userDetails));
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        // Проверяем, что пришел именно refresh token
        if (!jwtTokenUtil.isRefreshToken(refreshToken)) {
            throw new NotValidTokenException("Это не рефреш токен");
        }

        String username = jwtTokenUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtTokenUtil.validateToken(refreshToken, userDetails)) {
            throw new NotValidTokenException("Неверный токен");
        }
        // Генерируем новый access token и возвращаем его с имеющимся refresh token
        return new AuthenticationResponse(jwtTokenUtil.generateRefreshToken(userDetails),
                jwtTokenUtil.generateAccessToken(userDetails));
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        if (userService.existByUsername(registrationRequest.getUsername())) {
            throw new CredentialsAlreadyExistsException("Такое имя уже занято");
        }

        User user = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);
    }
}
