package com.cartit.dto.response;

public class AuthResponse {

    private String token;
    private boolean newUser;
    private UserResponse user;
    private String role;

    public AuthResponse(
            String token,
            boolean newUser,
            UserResponse user,
            String role) {

        this.token = token;
        this.newUser = newUser;
        this.user = user;
        this.role = role;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public String getToken() {
        return token;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getRole() {
        return role;
    }
}