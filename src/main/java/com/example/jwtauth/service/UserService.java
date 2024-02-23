package com.example.jwtauth.service;

import com.example.jwtauth.domain.entity.User;
import com.example.jwtauth.error.CredentialsAlreadyExistsException;
import com.example.jwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CredentialsAlreadyExistsException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::findByUsername;
    }

    public String getUsernameFromAuthorizedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    public boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    public List<String> getAllUsernames(){
        return userRepository.findAllUsernames();
    }

}
