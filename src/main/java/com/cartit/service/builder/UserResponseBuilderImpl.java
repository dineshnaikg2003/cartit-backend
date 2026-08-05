package com.cartit.service.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;

@Component
public class UserResponseBuilderImpl implements UserResponseBuilder {

    @Override
    public UserResponse build(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    @Override
    public List<UserResponse> build(List<User> users) {

        return users.stream()
                .map(this::build)
                .toList();
    }
}