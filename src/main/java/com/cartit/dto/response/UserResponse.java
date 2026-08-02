package com.cartit.dto.response;

import java.time.LocalDateTime;

import com.cartit.enums.Role;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(
            Long id,
            String name,
            String email,
            String phone,
            Role role,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}