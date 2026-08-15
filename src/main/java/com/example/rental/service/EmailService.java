package com.example.rental.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Car Rental - Email Verification OTP");

        message.setText(
                "Your OTP for email verification is: " + otp
                + "\n\n"
                + "This OTP is valid for 5 minutes."
                + "\n\n"
                + "If you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }
}
