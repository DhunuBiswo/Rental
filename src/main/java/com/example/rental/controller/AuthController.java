package com.example.rental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rental.dto.LoginRequestDto;
import com.example.rental.dto.LoginResponseDto;
import com.example.rental.dto.SignupRequestDto;
import com.example.rental.service.AuthService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody SignupRequestDto request) {

        return ResponseEntity.ok(
                authService.signup(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }
}
