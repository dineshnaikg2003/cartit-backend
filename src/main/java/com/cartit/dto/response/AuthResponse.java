package com.cartit.dto.response;

public class AuthResponse {

	private String token;
	private boolean newUser;
	private UserResponse user;

	public AuthResponse(
	        String token,
	        boolean newUser,
	        UserResponse user) {

	    this.token = token;
	    this.newUser = newUser;
	    this.user = user;
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
}