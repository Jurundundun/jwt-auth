package com.example.jwtauth.domain.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String refreshToken;
    private String accessToken;
}
