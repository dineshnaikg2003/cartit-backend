package com.cartit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.UpdateProfileRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.UserResponse;
import com.cartit.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {

        UserResponse response =
                userService.getProfile();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile fetched successfully.",
                        response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        UserResponse response =
                userService.updateProfile(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Profile updated successfully.",
                        response));
    }
}