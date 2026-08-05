package com.cartit.service.validator;

import org.springframework.stereotype.Component;

import com.cartit.dto.request.UpdateProfileRequest;
import com.cartit.entity.User;
import com.cartit.exception.BadRequestException;
import com.cartit.repository.UserRepository;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateUpdateProfile(
            User currentUser,
            UpdateProfileRequest request) {

        if (!currentUser.getPhone().equals(request.getPhone())
                && userRepository.existsByPhone(request.getPhone())) {

            throw new BadRequestException(
                    "Phone number is already registered.");
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()
                && !request.getEmail().equalsIgnoreCase(currentUser.getEmail())
                && userRepository.existsByEmailIgnoreCase(request.getEmail())) {

            throw new BadRequestException(
                    "Email is already registered.");
        }
    }
}