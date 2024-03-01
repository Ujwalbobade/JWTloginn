package com.example.JWTlogin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Builder
public record AuthResponse(String token, AuthStatus authStatus) {
}
