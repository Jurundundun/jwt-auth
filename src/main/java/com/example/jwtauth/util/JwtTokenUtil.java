package com.example.jwtauth.util;

import com.example.jwtauth.error.NotValidTokenException;
import com.example.jwtauth.service.user.UserService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.example.jwtauth.domain.enumm.TokenType.ACCESS;
import static com.example.jwtauth.domain.enumm.TokenType.REFRESH;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;
    private final UserService userService;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationMs, ACCESS.name());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationMs,REFRESH.name());
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (isTokenExpired(token)){
            throw new NotValidTokenException("Токен истек");
        }
        return username.equals(userDetails.getUsername());
    }

    private String generateToken(UserDetails userDetails, long expirationMs, String tokenType) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("type", tokenType)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    private boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token)
                    .getBody().getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (JwtException ex) {
            return true;
        }
    }
    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        return claims.containsKey("type") && claims.get("type").equals(REFRESH.name());
    }

    public UserDetails validateTokenAndGetUserDetails(String jwt) {
        String username = extractUsername(jwt);

        UserDetails userDetails = userService.loadUserByUsername(username);

        if (validateToken(jwt, userDetails)) {
            return userDetails;
        } else {
            throw new NotValidTokenException("Invalid or expired token");
        }
    }

}



