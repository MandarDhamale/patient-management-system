package com.pm.authservice.dto;

public class LoginResponseDTO {

    private final String token; // cannot override the token once initialized

    // since there is only one variable it's better to use a constructor than getter/setter, keep the code cleaner
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
