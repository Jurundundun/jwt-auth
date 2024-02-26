package com.example.jwtauth.service.user;

import com.example.jwtauth.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(User user);
    String getUsernameFromAuthorizedUser();
    boolean existByUsername(String username);
    List<String> getAllUsernames();
    User create(User user);
}
