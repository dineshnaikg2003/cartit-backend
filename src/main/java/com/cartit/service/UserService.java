package com.cartit.service;

import java.util.List;

import com.cartit.dto.request.UpdateProfileRequest;
import com.cartit.dto.response.UserResponse;

public interface UserService {

	UserResponse getProfile();

	UserResponse updateProfile(UpdateProfileRequest request);

	List<UserResponse> getAllUsers();

	UserResponse getUser(Long userId);

	UserResponse activateUser(Long userId);

	UserResponse deactivateUser(Long userId);
	
	UserResponse getUserByPhone(String phone);
	
}