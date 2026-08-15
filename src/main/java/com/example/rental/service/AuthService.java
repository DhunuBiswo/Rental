package com.example.rental.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rental.dto.LoginRequestDto;
import com.example.rental.dto.LoginResponseDto;
import com.example.rental.dto.SignupRequestDto;
import com.example.rental.dto.VerifyOtpRequestDto;
import com.example.rental.entity.EmailOtp;
import com.example.rental.entity.Users;
import com.example.rental.repository.EmailOtpRepository;
import com.example.rental.repository.UserRepository;
import com.example.rental.security.JwtService;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailOtpRepository emailOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;

    public AuthService(
            UserRepository userRepository,
            EmailOtpRepository emailOtpRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.emailOtpRepository = emailOtpRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    // =========================
    // SIGNUP
    // =========================

    public String signup(SignupRequestDto request) {

        Optional<Users> existingUser =
                userRepository.findByEmail(request.getEmail());

        Users user;

        if (existingUser.isPresent()) {

            user = existingUser.get();

            // Email is already verified
            if (user.isEmailVerified()) {
                throw new RuntimeException("Email already registered");
            }

            // Existing user is NOT verified.
            // Update the signup details.
            user.setName(request.getName());
            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
            user.setPhone(request.getPhone());
            user.setUserType(request.getUserType());

        } else {

            // Completely new user
            user = new Users();

            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
            user.setPhone(request.getPhone());
            user.setUserType(request.getUserType());
            user.setEmailVerified(false);
        }

        userRepository.save(user);

        // Generate new OTP
        String otp = generateOtp();

        EmailOtp emailOtp = new EmailOtp();

        emailOtp.setEmail(request.getEmail());
        emailOtp.setOtp(
                passwordEncoder.encode(otp)
        );
        emailOtp.setExpiresAt(
                LocalDateTime.now().plusMinutes(5)
        );
        emailOtp.setVerified(false);

        emailOtpRepository.save(emailOtp);

        // Send new OTP
        emailService.sendOtp(
                request.getEmail(),
                otp
        );

        return "OTP sent successfully to your email";
    }

    public String verifyOtp(VerifyOtpRequestDto request) {

        Users user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (user.isEmailVerified()) {
            return "Email already verified";
        }

        EmailOtp emailOtp = emailOtpRepository
                .findTopByEmailOrderByCreatedAtDesc(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new RuntimeException("OTP not found")
                );

        // Check expiry
        if (LocalDateTime.now()
                .isAfter(emailOtp.getExpiresAt())) {

            throw new RuntimeException("OTP expired");
        }

        // Check OTP
        if (!passwordEncoder.matches(
                request.getOtp(),
                emailOtp.getOtp())) {

            throw new RuntimeException("Invalid OTP");
        }

        // Mark OTP verified
        emailOtp.setVerified(true);
        emailOtpRepository.save(emailOtp);

        // Mark user email verified
        user.setEmailVerified(true);
        userRepository.save(user);

        return "Email verified successfully";
    }

    // =========================
    // LOGIN
    // =========================

    public LoginResponseDto login(LoginRequestDto request) {

        Users user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        // IMPORTANT
        // User must verify email before login

        if (!user.isEmailVerified()) {

            throw new RuntimeException(
                    "Please verify your email before login"
            );
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

    // =========================
    // GENERATE OTP
    // =========================

    private String generateOtp() {

        Random random = new Random();

        int otp = 100000 + random.nextInt(900000);

        return String.valueOf(otp);
    }
}