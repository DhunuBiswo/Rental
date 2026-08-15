package com.example.rental.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.rental.entity.EmailOtp;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {

    Optional<EmailOtp> findTopByEmailOrderByCreatedAtDesc(String email);
}
