package com.example.rental.dto;

public class LoginResponseDto {
	private String token;
    private Long userId;
    private String name;
    private String email;
    private String userType;

    public LoginResponseDto(String token, Long userId, String name,
                         String email, String userType) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }
}
