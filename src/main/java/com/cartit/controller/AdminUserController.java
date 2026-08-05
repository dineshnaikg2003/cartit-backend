package com.cartit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.UserResponse;
import com.cartit.service.UserService;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> response =
                userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Users fetched successfully.",
                        response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(
            @PathVariable Long id) {

        UserResponse response =
                userService.getUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User fetched successfully.",
                        response));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<UserResponse>> activateUser(
            @PathVariable Long id) {

        UserResponse response =
                userService.activateUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User activated successfully.",
                        response));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<UserResponse>> deactivateUser(
            @PathVariable Long id) {

        UserResponse response =
                userService.deactivateUser(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User deactivated successfully.",
                        response));
    }
    
    @GetMapping("/phone/{phone}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByPhone(
            @PathVariable String phone) {

        UserResponse response =
                userService.getUserByPhone(phone);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User fetched successfully.",
                        response));
    }
    
}