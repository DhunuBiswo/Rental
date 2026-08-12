package com.example.rental.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rental.dto.LoginRequestDto;
import com.example.rental.dto.LoginResponseDto;
import com.example.rental.dto.SignupRequestDto;
import com.example.rental.entity.Users;
import com.example.rental.repository.UserRepository;
import com.example.rental.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String signup(SignupRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        Users user = new Users();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        user.setPhone(request.getPhone());
        user.setUserType(request.getUserType());

        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponseDto login(LoginRequestDto request) {

        Users user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                    new RuntimeException("Invalid email or password")
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponseDto(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType()
        );
    }
}
