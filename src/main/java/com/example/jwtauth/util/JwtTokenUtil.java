package com.example.jwtauth.util;

import com.example.jwtauth.error.NotValidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenExpirationMs}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationMs,"access");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationMs,"refresh");
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
                .claim("type", tokenType) // Указываем тип токена
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
        // Проверяем наличие атрибута "type" и сравниваем его значение с "refresh"
        return claims.containsKey("type") && claims.get("type").equals("refresh");
    }

}



