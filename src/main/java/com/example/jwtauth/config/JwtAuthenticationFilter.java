package com.example.jwtauth.config;

import com.example.jwtauth.error.NotValidTokenException;
import com.example.jwtauth.error.resolver.ExceptionResolver;
import com.example.jwtauth.service.securitycontext.SecurityContextService;
import com.example.jwtauth.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtTokenUtil jwtTokenUtil;
    private final SecurityContextService securityContextService;
    private final ExceptionResolver exceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(BEARER_PREFIX.length());

        try {
            UserDetails userDetails = jwtTokenUtil.validateTokenAndGetUserDetails(jwt);
            securityContextService.authenticateUserInContextHolder(userDetails, request);
        } catch (NotValidTokenException e) {
            exceptionResolver.handleNotValidTokenException(request,response,e);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

