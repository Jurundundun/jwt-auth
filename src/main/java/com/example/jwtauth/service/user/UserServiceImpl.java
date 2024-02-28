package com.example.jwtauth.service.user;

import com.example.jwtauth.domain.entity.User;
import com.example.jwtauth.error.CredentialsAlreadyExistsException;
import com.example.jwtauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    @Override
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CredentialsAlreadyExistsException("Пользователь с таким email уже существует");
        }

        return save(user);
    }
    @Override
    public String getUsernameFromAuthorizedUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    @Override
    public boolean existByUsername(String username){
        return userRepository.existsByUsername(username);
    }
    @Override
    public List<String> getAllUsernames(){
        return userRepository.findAllUsernames();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}
