package com.cartit.service.builder;

import java.util.List;

import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;

public interface UserResponseBuilder {

    UserResponse build(User user);

    List<UserResponse> build(List<User> users);

}