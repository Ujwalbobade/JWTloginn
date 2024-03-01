package com.example.JWTlogin.Controller;

import com.example.JWTlogin.DTO.AuthRequest;
import com.example.JWTlogin.DTO.AuthResponse;
import com.example.JWTlogin.DTO.AuthStatus;
import com.example.JWTlogin.DTO.SignupRequest;
import com.example.JWTlogin.Service.AuthrizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
@Slf4j
public class AuthenticationController {
    private final AuthrizationService authrizationService;


    @PostMapping("/Signup")
    public ResponseEntity<AuthResponse> Signup(@RequestBody SignupRequest request) {
        log.info("Received signup request: {}", request);

        var token = authrizationService.SignUp(request.email(), request.password(), request.name(), request.role());
        var response = new AuthResponse(token, AuthStatus.USER_SUCCESSFULLY_CREATED);

        log.info("Signup successful for user: {}", request.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        log.info("Received login request: {}", request);

        var jwtToken = authrizationService.Login(request.email(), request.password());
        var response = new AuthResponse(jwtToken, AuthStatus.LOGIN_SUCCESS);

        log.info("Login successful for user: {}", request.email());
        return ResponseEntity.ok(response);
    }
}