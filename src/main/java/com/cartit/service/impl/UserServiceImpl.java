package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.UpdateProfileRequest;
import com.cartit.dto.response.UserResponse;
import com.cartit.entity.User;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.UserRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.UserService;
import com.cartit.service.builder.UserResponseBuilder;
import com.cartit.service.validator.UserValidator;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final UserValidator userValidator;
    private final UserResponseBuilder userResponseBuilder;

    public UserServiceImpl(
            UserRepository userRepository,
            CurrentUserService currentUserService,
            UserValidator userValidator,
            UserResponseBuilder userResponseBuilder) {

        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.userValidator = userValidator;
        this.userResponseBuilder = userResponseBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getProfile() {

        User user = currentUserService.getCurrentUser();

        return userResponseBuilder.build(user);
    }

    @Override
    public UserResponse updateProfile(
            UpdateProfileRequest request) {

        User user = currentUserService.getCurrentUser();

        userValidator.validateUpdateProfile(user, request);

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);

        return userResponseBuilder.build(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

    	return userResponseBuilder.build(
    	        userRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        return userResponseBuilder.build(user);
    }

    @Override
    public UserResponse activateUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));
        
        if (Boolean.TRUE.equals(user.getActive())) {
            throw new BadRequestException(
                    "User is already active.");
        }

        user.setActive(true);

        return userResponseBuilder.build(
                userRepository.save(user));
    }

    @Override
    public UserResponse deactivateUser(Long userId) {

    	User currentUser = currentUserService.getCurrentUser();

    	if (currentUser.getId().equals(userId)) {
    	    throw new BadRequestException(
    	            "You cannot deactivate your own account.");
    	}
    	
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        user.setActive(false);

        return userResponseBuilder.build(
                userRepository.save(user));
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByPhone(String phone) {

        User user = userRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return userResponseBuilder.build(user);
    }
    
}